package com.book.book;

import com.book.model.Category;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFilterDto {
    private int startIdx = 1;
    private int maxResults = 100;
    private String loginId;
    private Category category;
}
