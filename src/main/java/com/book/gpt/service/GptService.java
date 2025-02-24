package com.book.gpt.service;

import com.book.aladin.domain.AladinMaster;
import com.book.aladin.exception.AladinException;
import com.book.common.CommonUtil;
import com.book.common.service.CommonApiService;
import com.book.gpt.domain.GptRequest;
import com.book.gpt.domain.GptResponse;
import com.book.gpt.dto.GptParamDto;
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
public class GptService {
    @Qualifier("gptApi")
    private final WebClient gptApi;

    @Value("${gpt.api_key}")
    private String apiKey;

    private final CommonApiService commonApiService;

    public GptResponse getResponse(String path, GptParamDto gptParamDto) {
        ResponseEntity<GptResponse> response = null;
        GptRequest gptRequest = GptRequest.builder()
                .messages(gptParamDto.getGptMessageList())
                .build();
        MultiValueMap maps = CommonUtil.getApiParamMap(gptRequest);
            return commonApiService.getApi(gptApi,
                        path,
                        maps,
                        GptResponse.class,
                        apiKey
                    );
    }
}
