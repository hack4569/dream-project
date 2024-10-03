package com.book.aladin.service;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.common.ApiParam;
import com.book.recommend.dto.BookFilterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AladinService {
    @Value("${aladin.ttbkey}")
    private String ttbkey;

    private final WebClient aladinApi;

    /**
     * 상품 목록
     * @param bookFilterDto
     * @return
     */
    public Optional<List<AladinBook>> bookList(BookFilterDto bookFilterDto) {
        log.info("bookList :::::::::::::::::::::::::::::" + ttbkey);
        ApiParam apiParam = ApiParam.builder()
                .querytype(bookFilterDto.getQueryType())
                .start(bookFilterDto.getStartIdx())
                .ttbkey(ttbkey)
                .maxResults(bookFilterDto.getMaxResults()).build();
        return Optional.ofNullable(this.getApi(AladinConstants.ITEM_LIST, apiParam).getItem());
    }

    /**
     * 상품 상세 조회
     * @param apiParam
     * @return
     */
    public Optional<List<AladinBook>> getAladinDetail(ApiParam apiParam) {
        return Optional.ofNullable(this.getApi(AladinConstants.ITEM_LOOKUP, apiParam).getItem());
        //this.getApi(ITEM_LOOKUP, apiParam).orElseThrow(()-> new AladinException("상품 상세 조회 중 에러가 발생하였습니다."));
    }

    private AladinMaster getApi(String path, ApiParam apiParam) {
        ResponseEntity<AladinMaster> response = aladinApi
                .get()
                .uri(uriBuilder -> {
                    String url = uriBuilder
                        .path(path)
                        .queryParams(apiParam.getApiParamMap())
                        .build().toString();
                    log.info("get Api : " + url);
                    return URI.create(url);
                })
                .retrieve()
                .toEntity(AladinMaster.class)
                .block();
        return response.getBody();
    }
}
