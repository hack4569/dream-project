package com.book.recommend.constants;

public class RcmdConst {
    // 슬라이드에 표시되는 최소 글자 수
    public static final int strMinCount = 30;
    // 슬라이드에 표시되는 최대 글자 수
    public static final int strMaxCount = 150;

    //소개 페이지 개수
    public static final int introduceSlide = 2;
    //책속에서 페이지 개수
    public static final int paragraphSlide = 3;
    //노출 책 개수
    public static final int SHOW_BOOKS_COUNT = 2;

    public static final int THREAD_START_IDX = 1;
    public static final int THREAD_END_IDX = 2;

    public static final int NULL_PAGE_WAIT_COUNT = 5;

    public static final int getThredRemain() {
        return THREAD_END_IDX - THREAD_START_IDX + 1;
    }
}
