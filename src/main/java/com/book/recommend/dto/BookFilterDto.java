package com.book.recommend.dto;

import com.book.model.Category;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFilterDto {
    private int startIdx = 1;
    private int startN = 1;
    private int maxResults = 100;
    private long memberId;
    private Category category;

    public void setStartIdx(int startIdx) {
        if (this.startN == startIdx % 5) {
            this.startIdx = startIdx;
        }else {
            setStartIdx(startIdx+1);
        }
    }
}
