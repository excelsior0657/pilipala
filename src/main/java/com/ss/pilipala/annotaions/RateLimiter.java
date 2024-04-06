package com.ss.pilipala.annotaions;

import com.ss.pilipala.enums.LimitType;

import java.lang.annotation.*;

/**
 * 细粒度限流注解
 * 针对具体方法，可选 IP
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    String key() default "limit:";
    int seconds() default 5;
    int count() default 10;
    String message() default "访问过于频繁，请稍后重试";
    LimitType limitType() default LimitType.DEFAULT;
}
