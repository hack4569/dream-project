package com.book.aladin.support;

import com.book.aladin.exception.AladinException;
import com.book.common.ApiParam;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
public class AladinApiTemplate<T> {
    private Class<T> responseType;

    public AladinApiTemplate(Class<T> responseType) {
        this.responseType = responseType;
    }

    public T get(String uri, ApiParam apiParam) {
        T result = null;
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(uri)
                    .queryParams(apiParam.getApiParamMap())
                    .encode().build().toUri();
            RestTemplate rt = new RestTemplate();
            RequestEntity requestEntity = new RequestEntity(HttpMethod.GET, url);
            ResponseEntity<T> response = rt.exchange(requestEntity, responseType);
            //ResponseEntity<T> response = rt.exchange(requestEntity,new ParameterizedTypeReference<AladinMaster>(){});
            result = response.getBody();
        } catch (Exception e){
            String errorMsg = "get error {}";
            log.error(errorMsg, e);
            throw new AladinException("알라딘 api연동중 에러가 발생하였습니다.", e);
        }
        return result;
    }
}
