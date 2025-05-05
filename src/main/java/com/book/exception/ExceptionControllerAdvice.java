package com.book.exception;

import com.book.aladin.exception.AladinException;
import com.book.recommend.exception.RecommendExcption;
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
    @ExceptionHandler(UserException.class)
    public ResponseEntity exHandler(UserException e)
    {
        log.error("UserException error : {}", e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus())
                .body(e.getMessage());
    }
    @ExceptionHandler(AladinException.class)
    public ResponseEntity exHandler(AladinException e) {
        log.error("AladinException error : {}", e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(RecommendExcption.class)
    public void exHandler(RecommendExcption e) {
        log.error("RecommendExcption error : {}", e.getMessage(), e);
    }

    @ExceptionHandler(Exception.class)
    public void exHandler(Exception e) {
        log.error("RecommendExcption error : {}", e.getMessage(), e);
    }
}
