package com.book.recommend.aop;

import org.aspectj.lang.annotation.Pointcut;

public class RecommendPointcut {
    @Pointcut("execution(* com.book.recommend.controller..*(..))")
    public void checkSpeed(){}
}
