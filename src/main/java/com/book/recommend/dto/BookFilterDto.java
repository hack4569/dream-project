package com.book.recommend.dto;

import com.book.model.Category;
import com.book.model.History;
import com.book.recommend.constants.RcmdConst;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    private String queryType;
    private String filterType;
    private Category category;
    private Optional<HashSet<Integer>> finalCids;
    private Optional<String> anchorDate;
    private Optional<List<History>> histories;

    public void setStartIdx(int startIdx) {
        if (this.startN == startIdx % RcmdConst.THREAD_END_IDX + 1) {
            this.startIdx = startIdx;
        }else {
            setStartIdx(startIdx+1);
        }
    }
}
