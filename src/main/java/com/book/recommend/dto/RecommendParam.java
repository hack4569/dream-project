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
    private int slideN = 1;
    List<History> histories;
    HashSet<Integer> cids;

    public int getSlideN() {
        return slideN == 0 ? 1 : slideN;
    }
}
