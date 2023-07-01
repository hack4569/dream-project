package com.book.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserException extends Exception{
    public UserException(String errMsg){
        super(errMsg);
    }
}
