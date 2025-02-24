package com.book.aladin.service;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.aladin.exception.AladinException;
import com.book.common.ApiParam;
import com.book.common.CommonUtil;
import com.book.common.service.CommonApiService;
import com.book.recommend.dto.BookFilterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AladinService {
    @Value("${aladin.ttbkey}")
    private String ttbkey;

    @Qualifier("aladinApi")
    private final WebClient aladinApi;
    private final CommonApiService commonApiService;
    /**
     * 상품 목록
     * @param bookFilterDto
     * @return
     */
    public Optional<List<AladinBook>> bookList(BookFilterDto bookFilterDto) {
        ApiParam apiParam = ApiParam.builder()
                .querytype(bookFilterDto.getQueryType())
                .start(bookFilterDto.getStartIdx())
                .ttbkey(ttbkey)
                .maxResults(bookFilterDto.getMaxResults()).build();
        MultiValueMap maps = CommonUtil.getApiParamMap(apiParam);
        return Optional.ofNullable(commonApiService.getApi(aladinApi, AladinConstants.ITEM_LIST, maps, AladinMaster.class, "").getItem());
    }

    /**
     * 상품 상세 조회
     * @param apiParam
     * @return
     */
    public Optional<List<AladinBook>> getAladinDetail(ApiParam apiParam) {
        MultiValueMap maps = CommonUtil.getApiParamMap(apiParam);
        return Optional.ofNullable(commonApiService.getApi(aladinApi, AladinConstants.ITEM_LOOKUP, maps, AladinMaster.class, "").getItem());
        //this.getApi(ITEM_LOOKUP, apiParam).orElseThrow(()-> new AladinException("상품 상세 조회 중 에러가 발생하였습니다."));
    }

    private AladinMaster getApi(String path, ApiParam apiParam) {
        ResponseEntity<AladinMaster> response = null;
        try{
            response = aladinApi
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
        } catch (Exception e) {
            log.error("[알라딘] 에러 메세지 파싱 에러 code={}, errorMessage={}", response.getStatusCodeValue(), e.getMessage(), e);
            throw new AladinException("파싱에러", HttpStatus.valueOf(response.getStatusCodeValue()));
        }

    }
}
