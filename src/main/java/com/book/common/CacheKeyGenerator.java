package com.book.common;

import org.springframework.stereotype.Component;

@Component
public class CacheKeyGenerator {

    public static String generateBookDetailKey(String isbn13) {
        return "book:detail:" + isbn13;
    }

    public static String generateBookListKey(String queryType, int startIdx, String filterType) {
        return String.format("book:list:%s:%d:%s", queryType, startIdx, filterType);
    }

    public static String generateGptResponseKey(String bookTitle) {
        return "gpt:quote:" + bookTitle.replaceAll("[^a-zA-Z0-9가-힣]", "_");
    }

    public static String generateRecommendListKey(Long memberId, String categoryId, int slideN, String cids) {
        return String.format("recommend:list:%d:%s:%d:%s", memberId, categoryId, slideN, cids);
    }

    public static String generateFilteredBooksKey(String queryType, int startIdx, String filterType, String cids, String anchorDate) {
        return String.format("filtered:books:%s:%d:%s:%s:%s", queryType, startIdx, filterType, cids, anchorDate);
    }
}
