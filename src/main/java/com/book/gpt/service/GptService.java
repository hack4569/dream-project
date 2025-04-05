package com.book.gpt.service;

import com.book.aladin.constants.AladinConstants;
import com.book.aladin.domain.AladinMaster;
import com.book.aladin.exception.AladinException;
import com.book.common.CommonUtil;
import com.book.common.service.CommonApiService;
import com.book.gpt.domain.GptMessage;
import com.book.gpt.domain.GptRequest;
import com.book.gpt.domain.GptResponse;
import com.book.gpt.dto.GptParamDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {
    @Qualifier("gptApi")
    private final WebClient gptApi;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    public GptResponse chatGpt(String msg) {
        GptRequest request = GptRequest.builder()
                .messages(List.of(
                        new GptMessage("user", msg)
                )).build();

        try {
            log.info("요청 데이터: {}" + objectMapper.writeValueAsString(request));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<GptResponse> response = gptApi.post()
                .uri("/v1/chat/completions")
                .bodyValue(request)
                .retrieve()
                .toEntity(GptResponse.class)
                .block();
        return response.getBody();
    }
}
