package com.book.exception;

import com.book.aladin.exception.AladinException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

//패키지, 어노테이션, 특정 Controller 단위로 지정할 수 있음.
//basePackages = "com.book.recommend"
@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public void exHandler(UserException e) {
        log.error(e.getMessage(), e);
    }
    @ExceptionHandler
    public void exHandler(AladinException e) {
        log.error(e.getMessage(), e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public void exHandler1(Exception e) {
        log.error("Exception Error : {}", e);
    }
}
