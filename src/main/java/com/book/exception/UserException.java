package com.book.exception;

import lombok.NoArgsConstructor;

public class UserException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "[사용자 오류] ";

    public UserException() {
        super(DEFAULT_MESSAGE);
    }

    public UserException(String message) {
        super(DEFAULT_MESSAGE + "(" + message + ")");
    }
}
