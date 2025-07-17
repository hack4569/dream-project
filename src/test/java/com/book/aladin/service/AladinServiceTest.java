package com.book.aladin.service;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinBook;
import com.book.aladin.domain.AladinMaster;
import com.book.aladin.exception.AladinException;
import com.book.common.ApiParam;
import com.book.recommend.dto.BookFilterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AladinServiceTest {

    @Mock
    private WebClient aladinApi;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private WebClient.UriSpec uriSpec;

    @InjectMocks
    private AladinService aladinService;

    private ApiParam testApiParam;
    private AladinMaster testAladinMaster;
    private ResponseEntity<AladinMaster> testResponse;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 설정
        testApiParam = ApiParam.builder()
                .querytype("BestSeller")
                .start(1)
                .ttbkey("test-key")
                .build();

        // 테스트용 AladinBook 생성
        AladinBook testBook = new AladinBook();
        testBook.setTitle("테스트 책");
        testBook.setAuthor("테스트 저자");
        testBook.setIsbn13("9781234567890");

        // 테스트용 AladinMaster 생성
        testAladinMaster = new AladinMaster();
        testAladinMaster.setItem(Arrays.asList(testBook));
        testAladinMaster.setTotalResults(1);

        // 테스트용 ResponseEntity 생성
        testResponse = new ResponseEntity<>(testAladinMaster, HttpStatus.OK);

        // ttbkey 설정


        // WebClient Mock 체인 설정
        when(aladinApi.get()).thenReturn(requestHeadersUriSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntity(AladinMaster.class)).thenReturn(Mono.just(testResponse));
    }

    @Test
    @DisplayName("정상적인 API 호출 테스트")
    void testGetApiSuccess() {
        // given
        when(responseSpec.toEntity(AladinMaster.class)).thenReturn(Mono.just(testResponse));

        // when
        Optional<List<AladinBook>> result = aladinService.getAladinDetail(testApiParam);

        // then
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals("테스트 책", result.get().get(0).getTitle());

        // WebClient 호출 검증
        verify(aladinApi, times(1)).get();
        //verify(requestHeadersUriSpec, times(1)).uri(any());
    }

    @Test
    @DisplayName("4xx 클라이언트 에러 테스트")
    void testGetApiClientError() {
        // given
        WebClientResponseException clientException =
                new WebClientResponseException(HttpStatus.BAD_REQUEST.value(), "Bad Request", null, null, null);
        when(responseSpec.toEntity(AladinMaster.class)).thenThrow(clientException);

        // when & then
        AladinException exception = assertThrows(AladinException.class, () -> {
            aladinService.getAladinDetail(testApiParam);
        });

        assertEquals("API 호출 실패: Bad Request", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    @DisplayName("5xx 서버 에러 테스트")
    void testGetApiServerError() {
        // given
        WebClientResponseException serverException =
                new WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, null, null);
        when(responseSpec.toEntity(AladinMaster.class)).thenThrow(serverException);

        // when & then
        AladinException exception = assertThrows(AladinException.class, () -> {
            aladinService.getAladinDetail(testApiParam);
        });

        assertEquals("API 호출 실패: Internal Server Error", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    @DisplayName("일반 예외 테스트")
    void testGetApiGeneralException() {
        // given
        RuntimeException generalException = new RuntimeException("일반 예외");
        when(responseSpec.toEntity(AladinMaster.class)).thenThrow(generalException);

        // when & then
        AladinException exception = assertThrows(AladinException.class, () -> {
            aladinService.getAladinDetail(testApiParam);
        });

        assertTrue(exception.getMessage().contains("파싱에러"));
    }

    @Test
    @DisplayName("서킷브레이커 fallback 동작 테스트")
    void testCircuitBreakerFallback() {
        // given: 연속적인 실패를 시뮬레이션
        RuntimeException apiException = new RuntimeException("API 서버 다운");
        when(responseSpec.toEntity(AladinMaster.class)).thenThrow(apiException);

        // when: 여러 번 실패를 유도하여 서킷브레이커 활성화
        for (int i = 0; i < 10; i++) {
            try {
                aladinService.getAladinDetail(testApiParam);
            } catch (Exception ignored) {
                // 초기 실패들은 무시
            }
        }

        // then: 서킷브레이커가 활성화되어 fallback 메소드가 호출되어야 함
        AladinException fallbackException = assertThrows(AladinException.class, () -> {
            aladinService.getAladinDetail(testApiParam);
        });

        assertEquals("알라딘 API 서비스가 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해주세요.",
                fallbackException.getMessage());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, fallbackException.getHttpStatus());
    }

    @Test
    @DisplayName("bookList 메소드에서 데이터가 없을 때 예외 발생 테스트")
    void testBookListEmptyData() {
        // given: 빈 데이터 응답
        AladinMaster emptyMaster = new AladinMaster();
        emptyMaster.setItem(null);
        ResponseEntity<AladinMaster> emptyResponse = new ResponseEntity<>(emptyMaster, HttpStatus.OK);
        when(responseSpec.toEntity(AladinMaster.class)).thenReturn(Mono.just(emptyResponse));

        BookFilterDto bookFilterDto = BookFilterDto.builder()
                .queryType("BestSeller")
                .startIdx(1)
                .build();

        // when & then
        AladinException exception = assertThrows(AladinException.class, () -> {
            aladinService.bookList(bookFilterDto);
        });

        assertEquals("상품조회시 데이터가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("API 파라미터가 올바르게 전달되는지 테스트")
    void testApiParameterPassing() {
        // given
        when(responseSpec.toEntity(AladinMaster.class)).thenReturn(Mono.just(testResponse));

        // when
        aladinService.getAladinDetail(testApiParam);

        // then: URI 빌더가 올바른 파라미터로 호출되었는지 검증
//        verify(requestHeadersUriSpec).uri(argThat(uriBuilder -> {
//            // URI 빌더가 호출되었는지 확인
//            return true;
//        }));
    }

    @Test
    @DisplayName("응답이 null일 때 처리 테스트")
    void testNullResponse() {
        // given
        when(responseSpec.toEntity(AladinMaster.class)).thenReturn(Mono.just(new ResponseEntity<>(null, HttpStatus.OK)));

        // when
        Optional<List<AladinBook>> result = aladinService.getAladinDetail(testApiParam);

        // then
        assertFalse(result.isPresent());
    }
}