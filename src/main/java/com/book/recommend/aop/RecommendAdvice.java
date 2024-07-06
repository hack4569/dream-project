package com.book.recommend.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RecommendAdvice {
    @Around("com.book.recommend.aop.RecommendPointcut.checkSpeed()")
    public Object checkSpeed(ProceedingJoinPoint joinPoint) throws Throwable{
        try {
            long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
            Object result = joinPoint.proceed();
            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
            log.info("시간차이 : {}", secDiffTime);

            return result;
        } catch (Exception e) {
            log.info("RecommendAspect checkSpeed error:{}", e, e.getMessage());
            throw e;
        }
    }
}
