package com.book.gpt.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GptMessage {
    private String role;
    private String content;
    private Object refusal;
}
