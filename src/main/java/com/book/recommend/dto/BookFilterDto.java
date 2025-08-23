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
    private int maxResults = 10;
    private int showBooksCount = RcmdConst.SHOW_BOOKS_COUNT;
    private long memberId;
    private String queryType;
    private String filterType;
    private Category category;
    private Optional<HashSet<Integer>> finalCids;
    private Optional<String> anchorDate;
    private List<History> histories;

    public void setStartIdx(int startIdx) {
        if (getStartN() == startIdx % RcmdConst.THREAD_END_IDX + 1) {
            this.startIdx = startIdx;
        }else {
            setStartIdx(startIdx+1);
        }
    }

    public int getStartIdx() {
        return startIdx == 0 ? 1 : startIdx;
    }

    public int getStartN() {
        return startN == 0 ? 1 : startN;
    }
}
