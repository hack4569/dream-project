package com.book.recommend.service;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.MdRecommend;
import com.book.aladin.domain.Phrase;
import com.book.aladin.exception.AladinException;
import com.book.aladin.service.AladinService;
import com.book.category.dto.CategoryDto;
import com.book.common.ApiParam;
import com.book.common.BookRecommendUtil;
import com.book.gpt.domain.GptMessage;
import com.book.gpt.domain.GptRequest;
import com.book.gpt.domain.GptResponse;
import com.book.gpt.dto.GptParamDto;
import com.book.gpt.service.GptService;
import com.book.model.Category;
import com.book.model.History;
import com.book.model.Member;
import com.book.recommend.constants.RcmdConst;
import com.book.recommend.dto.BookFilterDto;
import com.book.recommend.dto.RecommendCommentDto;
import com.book.recommend.dto.RecommendDto;
import com.book.recommend.dto.RecommendParam;
import com.book.recommend.enumeration.BookFilter;
import com.book.recommend.exception.RecommendExcption;
import com.book.recommend.factory.FilterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final AladinService aladinService;
    private final GptService gptService;
    private final ModelMapper modelMapper = new ModelMapper();
    @Value("${aladin.ttbkey}")
    private String ttbkey;

    @Transactional
    public List<RecommendDto> getRecommendList(RecommendParam recommendParam) {

        Member loginMember = recommendParam.getMember();
        CategoryDto categoryDto = recommendParam.getCategoryDto();
        List<History> histories = recommendParam.getHistories();
        HashSet<Integer> cids = recommendParam.getCids();
        int slideN = recommendParam.getSlideN();
        List<RecommendDto> slideRecommendList = new ArrayList<>(); //사용자에게 보여줄 책추천리스트
        List<AladinBook> customFilteredBooks = new ArrayList<>();
        Category category = modelMapper.map(categoryDto, Category.class);
        if (category == null) {
            throw new RecommendExcption("카테고리가 없습니다.");
        }
        //category, loginId 세팅
        BookFilterDto bookFilterDto = BookFilterDto.builder()
                .memberId(loginMember.getId())
                .category(category)
                .startIdx(slideN)
                .startN(slideN)
                .queryType(Optional.ofNullable(loginMember.getQueryType()).orElse(AladinConstants.queryType))
                .filterType(Optional.ofNullable(loginMember.getFiterType()).orElse(BookFilter.Default.getName()))
                .build();

        //오늘 날짜
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
        String today = dateFormatter.format(cal.getTime());

        bookFilterDto.setFinalCids(Optional.ofNullable(cids));
        bookFilterDto.setAnchorDate(Optional.ofNullable(today));
        bookFilterDto.setHistories(Optional.ofNullable(histories));


        this.customFilteredList(customFilteredBooks, bookFilterDto);

        //책소개
        this.introduceBook(slideRecommendList, customFilteredBooks);

        return slideRecommendList;
    }

    public void introduceBook(List<RecommendDto> slideRecommendList, List<AladinBook> aladinBestSellerBooks) {
        for (int i = 0; i < aladinBestSellerBooks.size(); i++) {
            List<RecommendCommentDto> recommendCommentList = new ArrayList<>();
            ApiParam apiParam1 = ApiParam.builder()
                    .itemId(aladinBestSellerBooks.get(i).getIsbn13())
                    .ttbkey(ttbkey).build();
            List<AladinBook> aladinBooks = aladinService.getAladinDetail(apiParam1).orElseThrow(() -> new AladinException("상품 조회중 에러가 발생하였습니다."));

            AladinBook book = aladinBooks.get(0);

            //책소개
            String fullDescription = StringUtils.hasText(book.getFullDescription()) ? book.getFullDescription() : book.getFullDescription2();
            if (StringUtils.hasText(fullDescription)) {
                recommendCommentList = this.filterDescription(fullDescription, recommendCommentList, "description");
            }

            //편집자 추천
            List<MdRecommend> mdRecommendList = book.getSubInfo().getMdRecommendList();
            if (!ObjectUtils.isEmpty(mdRecommendList)) {
                for (MdRecommend mdRecommend : mdRecommendList) {
                    recommendCommentList = this.filterDescription(mdRecommend.getComment(), recommendCommentList, "mdRecommend");
                }
            }

            //gpt 책 속 명언 추출
            String bookName = book.getTitle() + " 책 속 명언 3개만 찾아서 '{quote1 : '명언1', quote2:'명언2'}'형태로 출력해줘";

            GptResponse gptResponse = gptService.chatGpt(bookName);



            String input = gptResponse != null && gptResponse.getChoices().size() > 0 ? gptResponse.getChoices().get(0).getMessage().getContent() : "";

            // 문자열을 Map<String, String>으로 변환
            input = input.replaceAll("[{}]", "");
            String[] pairs = input.split(", (?=quote)" ); // 정규 표현식 수정하여 올바르게 분리

            StringBuilder gptQuoteSb = new StringBuilder();
            for (String pair : pairs) {
                String[] entry = pair.split(": ", 2); // 두 번째 요소부터 모든 문자열 포함
                if (entry.length == 2) {
                    String gptQuote = entry[1].replaceAll("\"", "").trim();
                    gptQuoteSb.append(gptQuote);
                    gptQuoteSb.append("<br>");
                }
            }
            if (gptQuoteSb.length() != 0) {
                RecommendCommentDto recommendCommentQuote = RecommendCommentDto.builder()
                        .type("quote")
                        .content(gptQuoteSb.toString())
                        .build();

                recommendCommentList.add(recommendCommentQuote);
            }

            //책속에서 : 책 문장
            Phrase phrase;
            if (!ObjectUtils.isEmpty(book.getSubInfo().getPhraseList())) {
                int phraseLen = book.getSubInfo().getPhraseList().size();
                //j==0일 경우 이미지 확률이 높음
                for (int j = 1; j < phraseLen; j++) {

                    phrase = book.getSubInfo().getPhraseList().get(j);
                    String filteredPhrase = BookRecommendUtil.filterStr(phrase.getPhrase());
                    if (!StringUtils.hasText(filteredPhrase)) {
                        continue;
                    }
                    String[] phraseArr = filteredPhrase.split("\\.");
                    StringBuilder phraseContent = new StringBuilder();
                    int phraseArrLen = phraseArr.length < RcmdConst.paragraphSlide ? phraseArr.length : RcmdConst.paragraphSlide;
                    for (int k = 0; k < phraseArrLen; k++) {
                        phraseContent.append(phraseArr[k])
                                        .append(". ");
                    }
                    String phraseContentValue = phraseContent.toString();
                    RecommendCommentDto recommendCommentPhrase = RecommendCommentDto.builder()
                            .type("phrase")
                            .content(phraseContentValue)
                            .originContent(filteredPhrase)
                            .build();

                    recommendCommentList.add(recommendCommentPhrase);

                }

                if (recommendCommentList.size() == 0) continue;
            }

            // 목차
            String toc = book.getSubInfo().getToc();
            if (StringUtils.hasText(toc)) {
                toc = toc.replaceAll("<(/)?([pP]*)(\\s[pP]*=[^>]*)?(\\s)*(/)?>", "");
                RecommendCommentDto recommendToc = RecommendCommentDto.builder()
                                .type("toc")
                                        .content(toc)
                                                .build();
                recommendCommentList.add(recommendToc);
            }


            RecommendDto recommendDto = RecommendDto.builder()
                    .itemId(book.getItemId())
                    .title(book.getTitle())
                    .link(book.getLink())
                    .cover(book.getCover())
                    .recommendCommentList(recommendCommentList)
                    .author(book.getAuthor())
                    .categoryName(book.getCategoryName())
                    .build();
            slideRecommendList.add(recommendDto);

        }
    }


    private List<RecommendCommentDto> filterDescription(String description, List<RecommendCommentDto> recommendCommentList, String type) {
        //설명 첫번재 문단은 삭제
        String descriptionParagraph = descriptionParagraphFunc(description);

        //모든 태그 제거
        String filteredDescriptionParagraph = BookRecommendUtil.filterStr(descriptionParagraph);
        String[] descriptionArr = filteredDescriptionParagraph.split("\\.");
        List<String> descriptionList = Arrays.asList(descriptionArr);
        //글자가 많을 경우 2개 또는 ... 처리
        int introduceSlide = descriptionList.size() >= 3 && filteredDescriptionParagraph.length() > RcmdConst.strMaxCount * 2 ? RcmdConst.introduceSlide : 1;
        int slide = 0;
        for (int i = 0; i < introduceSlide; i++) {
            StringBuilder content = new StringBuilder();
            for (int j = 0; content.length() < RcmdConst.strMaxCount; j++) {
                if (descriptionList.size() <= slide ) {
                    break;
                }
                //int startIdx = descriptionList.size() >= 3 ? slide + 1 : slide;


                content.append(descriptionList.get(slide))
                        .append(". ");
                slide++;
            }
            String contentValue = content.toString();
            if (StringUtils.hasText(content)) {
                RecommendCommentDto recommendCommentDescription = RecommendCommentDto.builder()
                        .type(type)
                        .content(contentValue)
                        .originContent(description)
                        .build();

                recommendCommentList.add(recommendCommentDescription);
            }
        }

        return recommendCommentList;
    }

    private String descriptionParagraphFunc(String originParagraph) {

        if (StringUtils.hasText(originParagraph)) {
            if (originParagraph.toLowerCase().contains("<br>")) {
                List<String> paragraphList = Arrays.stream(originParagraph.toLowerCase().split("<br>"))
                        .filter(p -> StringUtils.hasText(p) && p.length() > 10)
                        .collect(Collectors.toList());
                //paragraphList.stream().collect(Collectors.joining(".")).toString();
                if (originParagraph.length() < 100 & paragraphList.size() < 4) {
                    return originParagraph;
                }else {
                    paragraphList.remove(0);
                    return paragraphList.stream().collect(Collectors.joining("<BR>")).toString();
                }
            }
        }
        return originParagraph;
    }

    public void customFilteredList(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto )
    {
        int nullPageCount = 0;
        while (aladinBooks.size() < RcmdConst.SHOW_BOOKS_COUNT) {
            List<AladinBook> filteredBooks = aladinService.bookList(bookFilterDto)
                    .orElseThrow(() -> new AladinException("베스트 상품 조회중 에러가 발생하였습니다."));

            FilterService filterService = FilterFactory.createFilter(bookFilterDto.getFilterType());
            List<AladinBook> result = filterService.filter(filteredBooks, bookFilterDto);

            if (result != null) {
                for (AladinBook book : result) {
                    if (aladinBooks.size() >= RcmdConst.SHOW_BOOKS_COUNT) break;
                    aladinBooks.add(book);
                }
            } else {
                if (nullPageCount == RcmdConst.NULL_PAGE_WAIT_COUNT) break;
                nullPageCount++;
            }
            // 다음 페이지 인덱스로 이동
            bookFilterDto.setStartIdx(bookFilterDto.getStartIdx() + 1);
        }
    }
}
