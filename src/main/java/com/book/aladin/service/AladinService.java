package com.book.aladin.service;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.aladin.exception.AladinException;
import com.book.common.ApiParam;
import com.book.recommend.dto.BookFilterDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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
    //@Cacheable(value = "bookList", key = "#bookFilterDto.queryType + ':' + #bookFilterDto.startIdx + ':' + #bookFilterDto.filterType")
    public Optional<List<AladinBook>> bookList(BookFilterDto bookFilterDto) {
        ApiParam apiParam = ApiParam.builder()
                .querytype(bookFilterDto.getQueryType())
                .start(bookFilterDto.getStartIdx())
                .ttbkey(ttbkey)
                .build();
        var aladinBooks = Optional.ofNullable(this.getApi(AladinConstants.ITEM_LIST, apiParam).getItem());
        if (aladinBooks.isEmpty()) {
            throw new AladinException("상품조회시 데이터가 없습니다.");
        }
        return aladinBooks;
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

    @CircuitBreaker(name = "aladinApi", fallbackMethod = "getApiFallback")
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
                    .onStatus(HttpStatus::is4xxClientError,
                            clientResponse -> Mono.error(new AladinException("클라이언트 오류", clientResponse.statusCode())))
                    .onStatus(HttpStatus::is5xxServerError,
                            clientResponse -> Mono.error(new AladinException("서버 오류", clientResponse.statusCode())))
                    .toEntity(AladinMaster.class)
                    .block();
            return response.getBody();
        } catch (WebClientResponseException e) {
            log.error("[알라딘] HTTP 에러: status={}, message={}", e.getStatusCode(), e.getMessage());
            throw new AladinException("API 호출 실패: " + e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            log.error("[알라딘] 에러 메세지 파싱 에러 code={}, errorMessage={}", response.getStatusCodeValue(), e.getMessage(), e);
            throw new AladinException("파싱에러", HttpStatus.valueOf(response.getStatusCodeValue()));
        }

    }

    private AladinMaster getApiFallback(String path, ApiParam apiParam, Exception exception) {
        log.warn("[알라딘] 서킷브레이커 활성화 - API 호출 실패: path={}, error={}", path, exception.getMessage());

        // 여기서는 기본값을 반환하거나 캐시된 데이터를 반환할 수 있습니다
        // 실제 구현에서는 Redis나 다른 캐시에서 데이터를 가져오는 로직을 추가할 수 있습니다

        throw new AladinException("알라딘 API 서비스가 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해주세요.",
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}
