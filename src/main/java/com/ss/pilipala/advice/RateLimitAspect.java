package com.ss.pilipala.advice;

import com.ss.pilipala.annotaions.RateLimiter;
import com.ss.pilipala.errors.BusinessException;
import com.ss.pilipala.interfaces.ILimitManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {
    private final ILimitManager limitManager;


    public RateLimitAspect(ILimitManager limitManager) {
        this.limitManager = limitManager;
    }

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter){
        if(!limitManager.tryAccess(rateLimiter)){
            throw BusinessException.businessError(rateLimiter.message());
        }
    }
}
