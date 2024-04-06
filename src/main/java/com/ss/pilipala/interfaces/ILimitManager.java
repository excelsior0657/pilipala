package com.ss.pilipala.interfaces;

import com.ss.pilipala.annotaions.RateLimiter;

public interface ILimitManager {
    boolean tryAccess(RateLimiter limiter);
}
