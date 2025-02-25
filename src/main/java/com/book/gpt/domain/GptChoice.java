package com.book.gpt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptChoice {
    private int index;
    private GptMessage message;

    @JsonProperty("finish_reason")
    private String finishReason;
}
