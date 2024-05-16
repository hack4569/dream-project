package com.book.policy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum QueryType {
    ITEM_NEW_ALL("itemNewAll", "신간 전체 리스트"),
    ITEM_NEW_SPECIAL("itemNewSpecial", "주목할 만한 신간 리스트"),
    BESTSELLER("bestSeller", "베스트 셀러"),
    BLOG_BEST("blogBest", "블러거 베스트 셀러");

    private String code;
    private String title;
}
