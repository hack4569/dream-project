package com.book.gpt.domain;

import com.book.common.CommonUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GptRequest {
    @Builder.Default
    private boolean store = true;
    @Builder.Default
    private String model = "gpt-4o-mini";

    private List<GptMessage> messages;


}
