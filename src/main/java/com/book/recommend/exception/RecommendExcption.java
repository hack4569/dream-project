package com.book.recommend.exception;

public class RecommendExcption extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "[책추천] " + "내부오류 발생";

    public RecommendExcption() {
        super(DEFAULT_MESSAGE);
    }

    public RecommendExcption(String message) {
        super(DEFAULT_MESSAGE + "(" + message + ")");
    }
}
