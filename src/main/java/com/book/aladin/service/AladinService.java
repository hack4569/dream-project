package com.book.aladin.service;

import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.common.ApiParam;
import com.book.recommend.dto.BookFilterDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AladinService {

    public static final String ITEM_LOOKUP = "/ttb/api/ItemLookUp.aspx";
    public static final String ITEM_LIST = "/ttb/api/ItemList.aspx";
    public static final String QUERY_TYPE = "BestSeller";

    private final WebClient aladinApi;

    /**
     * 상품 목록
     * @param bookFilterDto
     * @return
     */
    public Optional<List<AladinBook>> bookList(BookFilterDto bookFilterDto) {
        ApiParam apiParam = ApiParam.builder()
                .querytype(bookFilterDto.getQueryType())
                .start(bookFilterDto.getStartIdx())
                .maxResults(bookFilterDto.getMaxResults()).build();
        log.info("test4");
        return Optional.ofNullable(this.getApi(ITEM_LIST, apiParam).getItem());
    }

    /**
     * 상품 상세 조회
     * @param apiParam
     * @return
     */
    public Optional<List<AladinBook>> getAladinDetail(ApiParam apiParam) {
        return Optional.ofNullable(this.getApi(ITEM_LOOKUP, apiParam).getItem());
        //this.getApi(ITEM_LOOKUP, apiParam).orElseThrow(()-> new AladinException("상품 상세 조회 중 에러가 발생하였습니다."));
    }

    private AladinMaster getApi(String path, ApiParam apiParam) {
        ResponseEntity<AladinMaster> response = aladinApi
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(apiParam.getApiParamMap())
                        .build()
                )
                .retrieve()
                .toEntity(AladinMaster.class)
                .block();
        return response.getBody();
    }
}
