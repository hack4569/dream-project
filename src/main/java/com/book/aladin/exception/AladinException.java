package com.book.aladin.exception;

public class AladinException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "[알라딘 API] 연동 오류 발생";
    public AladinException() {
        super(DEFAULT_MESSAGE);
    }

    public AladinException(String message) {
        super(DEFAULT_MESSAGE + " (" + message + ") ");
    }

    public AladinException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE + " (" + message + ") ", cause);
    }

    public AladinException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

}
