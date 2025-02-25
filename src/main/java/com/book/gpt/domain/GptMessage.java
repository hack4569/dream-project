package com.book.gpt.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptMessage {
    private String role;
    private String content;
}
