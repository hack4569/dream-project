package com.book.aladin.service;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.aladin.exception.AladinException;
import com.book.common.ApiParam;
import com.book.recommend.dto.BookFilterDto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
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
    @CircuitBreaker(name = "aladinSearch", fallbackMethod = "searchFallBack")
    public Optional<List<AladinBook>> bookList(BookFilterDto bookFilterDto) {
        ApiParam apiParam = ApiParam.builder()
                .querytype(bookFilterDto.getQueryType())
                .start(bookFilterDto.getStartIdx())
                .ttbkey(ttbkey)
                .maxResults(bookFilterDto.getMaxResults()).build();
        var aladinBooks = Optional.ofNullable(this.getApi(AladinConstants.ITEM_LIST, apiParam).getItem());
        if (aladinBooks.isEmpty()) {
            throw new AladinException("상품조회시 데이터가 없습니다.");
        }
        return aladinBooks;
    }

    public Optional<List<AladinBook>> searchFallBack(BookFilterDto bookFilterDto, Throwable throwable) {
        // 서킷이 open상태가 되었을때
        if (throwable instanceof CallNotPermittedException) {
            return handleOpenCircuit(bookFilterDto);
        }
        return handleException(bookFilterDto, throwable);
    }

    private Optional<List<AladinBook>> handleOpenCircuit(BookFilterDto bookFilterDto) {
        log.warn("Circuit Breaker is open! Fallback to kakao search");
        return null;
    }

    private Optional<List<AladinBook>> handleException(BookFilterDto bookFilterDto, Throwable throwable) {
        log.error( "error occurred! Fallback to kakao search");
        return null;
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
