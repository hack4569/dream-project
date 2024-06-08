package com.book.recommend.service;

import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.MdRecommend;
import com.book.aladin.domain.Phrase;
import com.book.aladin.exception.AladinException;
import com.book.aladin.service.AladinService;
import com.book.category.dto.CategoryDto;
import com.book.category.service.CategoryService;
import com.book.common.ApiParam;
import com.book.common.BookRecommendUtil;
import com.book.history.repository.HistoryRepository;
import com.book.model.Category;
import com.book.model.History;
import com.book.policy.ListOptionPolicy;
import com.book.recommend.constants.RcmdConst;
import com.book.recommend.dto.BookFilterDto;
import com.book.recommend.dto.RecommendCommentDto;
import com.book.recommend.dto.RecommendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    @Value("${aladin.host}")
    String aladinHost;
    private final int SHOW_BOOKS_COUNT = 4;
    private final HistoryRepository historyRepository;
    private final CategoryService categoryService;
    private final AladinService aladinService;
    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public List<RecommendDto> getRecommendList(long memberId, CategoryDto categoryDto, int slideN) {

        List<RecommendDto> slideRecommendList = new ArrayList<>(); //사용자에게 보여줄 책추천리스트
        List<AladinBook> customFilteredBooks = new ArrayList<>();
        Category category = modelMapper.map(categoryDto, Category.class);
        //category, loginId 세팅
        BookFilterDto bookFilterDto = BookFilterDto.builder()
                .memberId(memberId)
                .category(category)
                .startN(slideN)
                .build();
        bookFilterDto.setStartIdx(1);

        this.customFilteredList(customFilteredBooks, bookFilterDto);

        //책소개
        this.introduceBook(slideRecommendList, customFilteredBooks);

        return slideRecommendList;
    }

    public void customFilteredList(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto) {

        try {

            List<AladinBook> aladinBestSellerBooks = aladinService.bestSellerList(bookFilterDto).orElseThrow(() -> new AladinException("베스트 상품 조회중 에러가 발생하였습니다."));

            String subCid = ObjectUtils.isEmpty(bookFilterDto.getCategory()) ? "" : bookFilterDto.getCategory().getSubCid();

            HashSet<Integer> cids = new HashSet<>();
            //사용자가 희망하는 카테고리가 있을 경우
            if (StringUtils.hasText(subCid)) {
//                List<Category> list = categoryMapper.getCategoryByParam(bookFilterDto.getCategory());
//                for (Category categoryMap : list) {
//                    String cid = Integer.toString(categoryMap.getCid());
//                    aladinAcceptCategoryList.add(cid);
//                }
            } else {
                cids = categoryService.findCategories();
            }

            //필터1 : 허용 카테고리만
            HashSet<Integer> finalCids = cids;
            aladinBestSellerBooks = aladinBestSellerBooks.stream().filter(i -> finalCids.contains(i.getCategoryId())).collect(Collectors.toList());

            //필터2 : 1년도 안된 책 out
            //오늘 날짜
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
            String today = dateFormatter.format(cal.getTime());
            aladinBestSellerBooks = aladinBestSellerBooks.stream().filter(i -> Integer.parseInt(today) > Integer.parseInt(this.getCustomDate(i.getPubDate()))).collect(Collectors.toList());

            //필터3 : history에 없는 데이터
            List<History> histories = historyRepository.findHistoryByMemberId(bookFilterDto.getMemberId());
            for (History history : histories) {
                aladinBestSellerBooks = aladinBestSellerBooks.stream().filter(bB ->
                    {
                        return
                        (
                            bB.getItemId() == history.getItemId()
                            &&
                            LocalDate.now().isEqual(history.getCreated().toLocalDate())
                        )
                        || bB.getItemId() != history.getItemId();
                    }).collect(Collectors.toList());
            }
            aladinBooks.addAll(aladinBestSellerBooks);

            if (aladinBooks.size() < SHOW_BOOKS_COUNT) {
                bookFilterDto.setStartIdx(bookFilterDto.getStartIdx() + 1);
                this.customFilteredList(aladinBooks, bookFilterDto);
            }
        } catch (AladinException ae) {
            throw ae;
        } catch (Exception e) {
            log.error("customFilteredList Error: {}", e);
            throw new AladinException(e.getMessage(), e);
        }
    }

    public void introduceBook(List<RecommendDto> slideRecommendList, List<AladinBook> aladinBestSellerBooks) {
        RestTemplate rt = new RestTemplate();
        for (int i = 0; i < aladinBestSellerBooks.size(); i++) {
            List<RecommendCommentDto> recommendCommentList = new ArrayList<>();
            int maxLength = 2;
            ApiParam apiParam1 = ApiParam.builder().itemId(aladinBestSellerBooks.get(i).getIsbn13()).build();

            List<AladinBook> aladinBooks = aladinService.getAladinDetail(apiParam1).orElseThrow(() -> new AladinException("상품 조회중 에러가 발생하였습니다."));

            AladinBook book = aladinBooks.get(0);

            //책소개
            String fullDescription = StringUtils.hasText(book.getFullDescription()) ? book.getFullDescription() : book.getFullDescription2();
            if (StringUtils.hasText(fullDescription)) {
                recommendCommentList = this.filterDescription(fullDescription, recommendCommentList, "description");
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
                    String phraseContent = "";
                    int phraseArrLen = phraseArr.length < RcmdConst.paragraphSlide ? phraseArr.length : RcmdConst.paragraphSlide ,ㅎㅎ하ㅕㅍv;
                    for (int k = 0; k < phraseArrLen; k++) {
                        phraseContent += phraseArr[k];
                    }
                    RecommendCommentDto recommendCommentPhrase = RecommendCommentDto.builder()
                            .type("phrase")
                            .content(phraseContent)
                            .originContent(filteredPhrase)
                            .build();

                    recommendCommentList.add(recommendCommentPhrase);

                }

                if (recommendCommentList.size() == 0) continue;
            }

            // 목차
            String toc = book.getSubInfo().getToc();
            if (toc != null) {
                toc = toc.replaceAll("<(/)?([pP]*)(\\s[pP]*=[^>]*)?(\\s)*(/)?>", "");
                RecommendCommentDto recommendToc = RecommendCommentDto.builder()
                                .type("toc")
                                        .content(toc)
                                                .build();
                recommendCommentList.add(recommendToc);
            }

            List<MdRecommend> mdRecommendList = book.getSubInfo().getMdRecommendList();
            if (!ObjectUtils.isEmpty(mdRecommendList)) {
                for (MdRecommend mdRecommend : mdRecommendList) {
                    recommendCommentList = this.filterDescription(mdRecommend.getComment(), recommendCommentList, "mdRecommend");
                }
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

    //설명 첫번재 문단은 삭제
    private List<RecommendCommentDto> filterDescription(String description, List<RecommendCommentDto> recommendCommentList, String type) {

        String originParagraph = description.replaceAll("(?i)<br>", "");
        String descriptionParagraph = descriptionParagraphFunc(originParagraph);

        String filteredDescriptionParagraph = BookRecommendUtil.filterStr(descriptionParagraph);
        String[] descriptionArr = filteredDescriptionParagraph.split("\\.");
        List<String> descriptionList = Arrays.asList(descriptionArr);
        //글자가 많을 경우 2개 또는 ... 처리
        int slide = 0;
        int introduceSlide = descriptionList.size() >= 5 && filteredDescriptionParagraph.length() > RcmdConst.strMaxCount * 2 ? RcmdConst.introduceSlide : 1;

        for (int i = 0; i < introduceSlide; i++) {
            String content = "";
            for (int j = 0; content.length() < RcmdConst.strMaxCount; j++) {
                if (descriptionList.size() <= slide ) {
                    break;
                }
                content += descriptionList.get(slide);
                slide++;
            }
            if (StringUtils.hasText(content)) {
                RecommendCommentDto recommendCommentDescription = RecommendCommentDto.builder()
                        .type(type)
                        .content(content)
                        .originContent(description)
                        .build();

                recommendCommentList.add(recommendCommentDescription);
            }
        }

        return recommendCommentList;
    }

    private String descriptionParagraphFunc(String originParagraph) {
        if ( originParagraph.length() > RcmdConst.strMaxCount ) return originParagraph;
        if ( originParagraph.length() < RcmdConst.strMinCount ) return "";
        return originParagraph;
    }



    private String getCustomDate(String yyyymmdd) {
        int year = Integer.parseInt(yyyymmdd.substring(0, 4));
        int month = Integer.parseInt(yyyymmdd.substring(5, 7));
        int date = Integer.parseInt(yyyymmdd.substring(8, 10));

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
        return dateFormatter.format(cal.getTime());
    }
}
