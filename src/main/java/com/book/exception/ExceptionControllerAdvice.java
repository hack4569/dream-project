package com.book.exception;

import com.book.recommend.RecommendController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//패키지, 어노테이션, 특정 Controller 단위로 지정할 수 있음.
//basePackages = "com.book.recommend"
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ErrorResult> exHandler(UserException e){
        log.error("UserException Error : {}", e);
        ErrorResult errorResult = new ErrorResult(e.getMessage());
        return new ResponseEntity<ErrorResult>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<ErrorResult> exHandler(Exception e){
        log.error("Exception Error : {}", e);
        ErrorResult errorResult = new ErrorResult(e.getMessage());
        return new ResponseEntity<ErrorResult>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
