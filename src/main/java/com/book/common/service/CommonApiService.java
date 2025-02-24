package com.book.common.service;

import com.book.aladin.domain.AladinMaster;
import com.book.aladin.exception.AladinException;
import com.book.common.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class CommonApiService {

    public <T> T getApi(WebClient webClient,
                        String path,
                        MultiValueMap<String, String> queryParams,
                        Class<T> responseType,
                        String authTokens) {
        ResponseEntity<T> response = null;
        WebClient.RequestHeadersSpec<?> request = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(queryParams)
                        .build()
                );
        if (authTokens != null && StringUtils.hasText(authTokens)) {
            request.header("Authorization", "Bearer " + authTokens);
        }
        response = request.retrieve()
                .toEntity(responseType)
                .block();
        return response.getBody();
    }
}
