package com.book.gpt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GptChoice {
    private int index;
    private GptMessage message;
    private Object logprobs;

    @JsonProperty("finish_reason")
    private String finishReason;
}
