package com.book.recommend.dto;

import com.book.category.dto.CategoryDto;
import com.book.model.History;
import com.book.model.Member;
import lombok.*;

import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendParam {
    private Member member;
    private CategoryDto categoryDto;
    private int slideN;
    List<History> histories;
    HashSet<Integer> cids;
}
