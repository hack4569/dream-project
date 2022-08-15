package com.book.recommend;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendDto {
    private long itemId;
    private List<RecommendCommentDto> recommendCommentList;
    private String title;
    private String link;
    private String cover;
}
