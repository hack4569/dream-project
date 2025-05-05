package com.book.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException{
    private final String errorMessage;
    private final HttpStatus httpStatus;


    private static final String DEFAULT_MESSAGE = "[사용자 오류] ";

    public UserException(String errorMessage, HttpStatus httpStatus) {
        super(DEFAULT_MESSAGE);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
