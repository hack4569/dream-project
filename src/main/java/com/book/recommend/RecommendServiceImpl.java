package com.book.recommend;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.aladin.domain.MdRecommend;
import com.book.aladin.domain.Phrase;
import com.book.aladin.exception.AladinException;
import com.book.aladin.support.AladinApiTemplate;
import com.book.book.BookFilterDto;
import com.book.common.ApiCommonUtil;
import com.book.common.ApiParam;
import com.book.common.BookRecommendUtil;
import com.book.common.IntHolder;
import com.book.model.Category;
import com.book.model.History;
import com.book.model.Recommend;
import com.book.model.mapper.CategoryMapper;
import com.book.recommend.constants.RcmdConst;
import com.book.recommend.exception.RecommendExcption;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("recommendService")
public class RecommendServiceImpl implements RecommendService {
    @Value("${aladin.host}")
    String aladinHost;
    @Value("${aladin.accept-category}")
    String aladinAcceptCategory;

    @Autowired
    private RecommendRepository recommendRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private AladinConstants aladinConstants;

    @Override
    public List<RecommendDto> getRecommendList(long memberId, Category category) {

        List<RecommendDto> slideRecommendList = new ArrayList<>(); //사용자에게 보여줄 책추천리스트
        List<AladinBook> customFilteredBooks = new ArrayList<>();

        //category, loginId 세팅
        BookFilterDto bookFilterDto = BookFilterDto.builder()
                .memberId(memberId)
                .category(category)
                .build();

        customFilteredBooks = this.customFilteredList(customFilteredBooks, bookFilterDto);

        //책소개
        this.introduceBook(slideRecommendList, customFilteredBooks);

        return slideRecommendList;
    }

    public List<AladinBook> bestSellerList(BookFilterDto bookFilterDto) {

        ApiParam apiParam = ApiParam.builder()
                .querytype("BestSeller")
                .start(bookFilterDto.getStartIdx())
                .maxResults(bookFilterDto.getMaxResults()).build();
        AladinApiTemplate<AladinMaster> aladinApiTemplate = new AladinApiTemplate<>(AladinMaster.class);
        AladinMaster aladinMaster = aladinApiTemplate.get(aladinConstants.url(AladinConstants.ITEM_LIST), apiParam);

        List<AladinBook> aladinBestSellerBooks = aladinMaster.getItem();
        return aladinBestSellerBooks;
    }

    public List<AladinBook> customFilteredList(List<AladinBook> aladinBooks, BookFilterDto bookFilterDto) {

        try {
            List<AladinBook> aladinBestSellerBooks = this.bestSellerList(bookFilterDto);


            final List<String> aladinAcceptCategoryList = new ArrayList<>();

            String subCid = ObjectUtils.isEmpty(bookFilterDto.getCategory()) ? "" : bookFilterDto.getCategory().getSubCid();
            //사용자가 희망하는 카테고리가 있을 경우
            if (StringUtils.hasText(subCid)) {
                List<Category> list = categoryMapper.getCategoryByParam(bookFilterDto.getCategory());
                for (Category categoryMap : list) {
                    String cid = Integer.toString(categoryMap.getCid());
                    aladinAcceptCategoryList.add(cid);
                }
            } else {
                String aladinAcceptCategory = "1230,170,3057,3060,3062,3058,3059,8586,3065,3140,8587,2172,3103,2173,274,284,8593,247,282,3069,2028,853,852,854,261,268,273,1632,55058,2169,263,172,141092,11502,175,11501,2225,174,177,3048,32828,180,249,3049,3104,2408,3101,11503,6189,2105,2330,4527,2094,2344,2310,2095,2123,4378,4655,12390,2124,4438,4437,4436,2125,2149,2314,2138,222,2126,2121,4440,4441,7459,987,51038,51365,51355,51357,51358,51361,51363,51007,51005,51160,51095,51098,51183,51187,51189,51185,51010,51002,51045,120980,120981,51013,51194,51197,51022,51285,51283,51290,51281,51287,51289,51279,51288,51024,51293,51301,51294,51291,51296,51298,51299,51292,51043,51275,51015,51202,51205,51208,51209,51199,51039,51033,51334,51329,51331,51324,51327,51272,51017,51030,51318,51315,51317,51027,51307,51310,51312,51035,51337,51339,51340,51342,51349,51335,51351,51353,798,51052,50999,51121,51117,51111,69510,51113,51119,51050,51396,51411,51401,66543,51407,51008,51046,51364,51372,51360,51356,51348,51359,51350,51366,51368,51369,51362,51352,51354,50992,51068,51061,51066,51064,51059,50995,51086,51078,51088,51084,51073,51090,51075,51074,51081,51004,51141,51161,51162,51159,51150,51145,51147,51154,51149,51144,51152,51001,51129,51136,65238,51131,51134,51026,51314,51308,51311,51304,51306,51041,51346,51344,51345,51347,51037,51341,51343,51326,51330,51333,51336,51338,51016,51231,51238,51243,51240,51241,51248,51246,51251,51224,51165,51166,51217,51228,51220,51233,51226,51011,51163,51164,51048,51382,51379,51021,51270,51268,51274,51266,51261,51263,51277,50997,51094,51097,51101,1,50930,89481,89482,50922,50927,51116,88136,106545,54719,51080,162391,51107,121541,89171,112097,51083,114691,111196,110488,51071,51077,87919,128346,112098,51099,51102,51070,138767,51105,148769,148770,109725,89172,89168,154903,89166,51114,168549,51106,130122,115694,55624,51069,89167,89169,89170,52650,50935,51126,51125,124234,50932,50951,51234,51218,51215,52653,51225,51230,51221,51229,157672,51223,51227,51213,51232,52654,50945,51193,51179,51206,51191,51204,51319,51320,51321,51322,51323,51325,51328,51332,51186,51190,51180,51196,51184,51182,51201,51309,51305,51302,51303,51300,51297,51295,51316,51313,51198,50955,51286,51265,51273,51267,51258,51276,51262,51269,51284,51259,51282,51271,51278,51255,51256,51280,51264,51260,50925,51055,51036,51032,52652,51044,51023,51040,50920,50940,69256,51170,51169,51172,51173,51168,51171,51167,50942,50933,51252,50929,50919,50943,51175,51177,50918,50996,50998,50923,50926,51067,51062,51058,51065,50953,51237,51245,51244,51242,51250,51235,51247,51236,51239,51253,50928,51120,51122,50921,50917,50994,50993,50931,50948,51212,51210,55889,51392,51380,51416,52906,52904,52905,51374,51398,51391,51376,51375,51377,51423,51425,51842,51843,51844,51845,51373,51404,51402,51394,51413,51389,51431,51427,51429,51408,51371,74,78,1628,2177,4867,4869,147,5242,52596,52598,52597,94,52599,1806,2031,169,5029,160,161,148,3802,36868,158,151,5445,5027,52604,52605,75,76,1438,159,165,52602,3938,52600,163,52603,167,52606,52607,52609,52608,52595,5558,5557,5559,52610,52614,52611,52613,52615,52617,52616,52589,121,130,1955,116,117,118,52594,1960,3826,1961,1962,3825,81,88,27795,2032,5618,89,90,2211,5566,4167,17897,84,52620,1963,5718,134,87,3266,109,113,4206,111,112,2215,2216,2217,2218,1442,52590,3431,141,52591,1753,52593,656,51378,51409,51541,51542,51543,51412,51393,51493,51501,51502,51503,51500,51495,51494,51504,51505,51506,51507,51508,51509,51498,51499,51497,51496,51510,51511,51512,51513,51417,51547,51549,51551,51550,51555,51548,51552,51557,51553,51556,51558,51559,51560,51561,51562,51554,65237,51390,51458,51459,51460,51462,51442,51450,51444,51466,51467,51468,51469,51470,51471,51472,51473,51474,51454,51456,2547,51457,51446,51451,51455,51449,51447,51443,51463,51464,51465,51453,51445,51475,51476,51477,51478,51479,51480,51481,51482,51483,51484,51485,51486,51487,51488,51489,51490,51491,51492,51452,51448,51405,51540,51539,51399,51523,51530,51526,51532,51533,51534,51527,51528,51531,51529,51535,51536,51524,51525,51395,51514,51520,51521,51515,51518,51522,51519,51516,51517,51415,51544,51546,51545,51384,51381,51385,51403,51538,51537,51387,51440,51441,51419,336,70241,70228,70229,70230,70212,70232,70213,70214,70215,70216,70220,70221,70222,70218,2951,5247,3812,2952,107822,70219,70223,2943,2944,147602,2946,70233,70234,70235,70211,70224,70226,70225,70227,70236,70239,70238,2030,73196,51763,90418,90548,90419,51776,51772,51766,7253,51792,7232,118496,7259,2923,85850,85853,106628,51790,51786,51785,51795,51762,90135,4241,120319,51769,89376,106819,51770,51789,121898,115698,107986,51773,86029,51768,51798,51800,51797,51801,51799,51765,51784,51783,51764,50013,51782,50012,51777,51767,51793,90007,3390,107331,7257,4288,51794";
                List<String> newAladinAcceptCategoryList = Arrays.asList(aladinAcceptCategory.split(","));
                for (String newAladinAcceptCategory : newAladinAcceptCategoryList) {
                    aladinAcceptCategoryList.add(newAladinAcceptCategory);
                }
            }


            //필터1 : 허용 카테고리만
            aladinBestSellerBooks = aladinBestSellerBooks.stream().filter(i -> aladinAcceptCategoryList.contains(Integer.toString(i.getCategoryId()))).collect(Collectors.toList());

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

            int startIdx = bookFilterDto.getStartIdx();
            if (startIdx >= 20) {
                return aladinBooks;
            }
            if (aladinBooks.size() < 5) {
                bookFilterDto.setStartIdx(++startIdx);
                this.customFilteredList(aladinBooks, bookFilterDto);
            }
        } catch (AladinException ae) {
            throw ae;
        } catch (Exception e) {
            log.error("customFilteredList Error: {}", e);
            throw new AladinException(e.getMessage(), e);
        }
        return aladinBooks;
    }

    public void introduceBook(List<RecommendDto> slideRecommendList, List<AladinBook> aladinBestSellerBooks) {
        RestTemplate rt = new RestTemplate();
        for (int i = 0; i < aladinBestSellerBooks.size(); i++) {
            List<RecommendCommentDto> recommendCommentList = new ArrayList<>();
            int maxLength = 2;
            ApiParam apiParam1 = ApiParam.builder().itemId(aladinBestSellerBooks.get(i).getIsbn13()).build();

            URI url = UriComponentsBuilder.fromHttpUrl(aladinConstants.url(AladinConstants.ITEM_LOOKUP))
                    .queryParams(apiParam1.getApiParamMap()).encode().build().toUri();
            RequestEntity requestEntity1 = new RequestEntity(HttpMethod.GET, url);

            ResponseEntity<AladinMaster> response = rt.exchange(requestEntity1, new ParameterizedTypeReference<AladinMaster>() {
            });
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new AladinException("api 통신중 에러가 발생하였습니다.");
            }
            AladinMaster aladinMaster = response.getBody();

            if (!ObjectUtils.isEmpty(aladinMaster.getItem())) {
                AladinBook book = aladinMaster.getItem().get(0);

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
    }

    //설명 첫번재 문단은 삭제
    private List<RecommendCommentDto> filterDescription(String description, List<RecommendCommentDto> recommendCommentList, String type) {

        String originParagraph = description.replaceAll("(?i)<br>", "");
        String descriptionParagraph = descriptionParagraphFunc(originParagraph);

        String filteredDescriptionParagraph = BookRecommendUtil.filterStr(descriptionParagraph);
        String[] descriptionArr = filteredDescriptionParagraph.split("\\.");
        //글자가 많을 경우 2개 또는 ... 처리
        int slide = 0;
        int introduceSlide = descriptionArr.length >= 5 && filteredDescriptionParagraph.length() > RcmdConst.strMaxCount * 2 ? RcmdConst.introduceSlide : 1;

        for (int i = 0; i < introduceSlide; i++) {
            String content = "";
            for (int j = 0; content.length() < RcmdConst.strMaxCount; j++) {
                if (descriptionArr.length < slide ) {
                    break;
                }
                content += descriptionArr[slide];
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
//        if ( paragraph.length() > RcmdConst.strMaxCount) {
//            if (paragraph.length() < RcmdConst.strMinCount){
//                return "";
//            } else {
//                return paragraph;
//            }
//        } else {
//            paragraph += brArr[intHolder.value];
//            if (brArr[intHolder.value].length() < RcmdConst.strMinCount || this.periodCount(brArr[intHolder.value]) < 2) {
//                descriptionParagraphFunc(brArr, intHolder, paragraph);
//            }
//            return paragraph;
//        }
    }

    private int periodCount(String paragraph) {
        int count = 0;
        for(char str : paragraph.toCharArray()) {
            if (count >= 2) {
                break;
            }
            if (str == '.') {
                count ++;
            }
        }
        return count;
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

    @Override
    public List<RecommendVO> recommendList2(String userId) {
        String uri = aladinHost + "/ttb/api/ItemList.aspx";
        RestTemplate rt = new RestTemplate();
        ApiParam apiParam = ApiParam.builder().querytype("BestSeller").build();
        URI url = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParams(apiParam.getApiParamMap())
                .encode().build().toUri();
        RequestEntity requestEntity = new RequestEntity(HttpMethod.GET, url);
        ResponseEntity<AladinMaster> response = rt.exchange(requestEntity, new ParameterizedTypeReference<AladinMaster>() {
        });

        AladinMaster aladinMaster = response.getBody();

        List<String> aladinAcceptCategoryList = Arrays.asList(aladinAcceptCategory.split(","));

        List<AladinBook> aladinBestSellerBooks = aladinMaster.getItem();

        aladinBestSellerBooks.stream().filter(i -> aladinAcceptCategoryList.contains(i.getCategoryId()));

        System.out.println(aladinBestSellerBooks);
        return null;
    }

    @Override
    public Object getSearchBookList(HttpServletRequest request, ApiParam apiParam) {
        String query = request.getParameter("query");
        String itemId = request.getParameter("itemId");
        String queryType = "Title";

        String uri = aladinHost + "/ttb/api/ItemSearch.aspx";
        RestTemplate rt = new RestTemplate();
        //rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        URI url = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParams(apiParam.getApiParamMap())
                .encode().build().toUri();
        RequestEntity requestEntity = new RequestEntity(HttpMethod.GET, url);
        ResponseEntity response = rt.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });
        if (!ApiCommonUtil.isConnected(response)) {
            return null;
        }

        return response.getBody();
    }

    @Override
    public Map<String, Object> selectDetail(int itemId) {
        String uri = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbhack45691028002&itemIdType=itemId&ItemId=" + itemId + "&output=js&Version=20131101&OptResult=reviewList";
        Map<String, Object> map = new HashMap<>();
        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = br.readLine();

            JSONParser parser = new JSONParser();
            JSONObject ja = null;

            ja = (JSONObject) parser.parse(line);
            map = getMapFromJSONObject(ja);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

//    @Override
//    public int saveRecommend(RecommendVO recommendVO) {
//        return recommendDAO.saveRecommend(recommendVO);
//    }

    @Override
    public Recommend saveRecommend(Recommend recommend) {
        return recommendRepository.save(recommend);
    }

//    @Override
//    public History saveHistory(History history) {
//        List<History> histories = historyRepository.findHistoryByLoginId(history.getLoginId());
//        histories = histories.stream().filter(j -> j.getItemId() == history.getItemId()).collect(Collectors.toList());
//        if (histories.size() > 0) {
//            return new History();
//        }
//        return historyRepository.save(history);
//    }

    public static Map<String, Object> getMapFromJSONObject(JSONObject obj) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format("BAD REQUEST obj %s", obj));
        }
        try {
            return new ObjectMapper().readValue(obj.toJSONString(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map changeToRbMap(Map map) {
        ArrayList list = (ArrayList) map.get("item");
        Map sMap = (HashMap<String, Object>) list.get(0);
        map.putAll(sMap);
        return map;
    }
}
