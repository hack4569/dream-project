package com.book.gpt.dto;

import com.book.gpt.domain.GptMessage;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptParamDto {
    private boolean store;
    private List<GptMessage> gptMessageList;
}
