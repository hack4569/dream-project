package com.book.aladin.service

import com.book.aladin.constants.AladinConstants
import com.book.recommend.dto.BookFilterDto
import com.book.recommend.enumeration.BookFilter
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Import(AladinServiceTest.TestConfig.class)
@SpringBootTest
class AladinServiceTest extends Specification {
    @Autowired
    AladinService aladinService

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry

    @Autowired
    String aladinTtbKey;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public String aladinTtbKey() {
            return "ttbhack45691028002";
        }
    }

    def "circuit breaker opens and returns null."() {
        given:
        def bookFilterDto = BookFilterDto.builder()
                .startIdx(1)
                .startN(1)
                .queryType("Title")
                .filterType("default")
                .build()
        def start = 1
        def ttbkey = this.aladinTtbKey
        def queryType = "Title"

        def config = CircuitBreakerConfig.custom()
                .slidingWindowSize(1)
                .minimumNumberOfCalls(1)
                .failureRateThreshold(50)
                .build()

        circuitBreakerRegistry.circuitBreaker("aladinSearch", config)

        when:
        def result = aladinService.bookList(bookFilterDto)

        then: "circuit이 OPEN된다."
        def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get()
        circuitBreaker.state == CircuitBreaker.State.OPEN

        and:
        result == null
    }
}
