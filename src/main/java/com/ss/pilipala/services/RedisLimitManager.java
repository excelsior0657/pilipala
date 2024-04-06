package com.ss.pilipala.services;

import com.ss.pilipala.annotaions.RateLimiter;
import com.ss.pilipala.enums.LimitType;
import com.ss.pilipala.errors.BusinessException;
import com.ss.pilipala.interfaces.ILimitManager;
import com.ss.pilipala.utils.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 使用 Redis 实现细粒度限流管理
 * 主要逻辑在该类
 */
@Slf4j
@Service
public class RedisLimitManager implements ILimitManager {

    private final HttpServletRequest request;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Long> redisScript;

    public RedisLimitManager(
            HttpServletRequest request,
            RedisTemplate<String, Object> redisTemplate,
            RedisScript<Long> redisScript) {
        this.request = request;
        this.redisTemplate = redisTemplate;
        this.redisScript = redisScript;
    }

    @Override
    public boolean tryAccess(RateLimiter limiter) {
        String key = limiter.key();
        if(StringUtils.isBlank(key)){
            throw BusinessException.businessError("redis key不能为空");
        }

        if(Objects.equals(limiter.limitType(), LimitType.IP)){
            String ip = RequestUtil.getIpAddress(request);
            key = String.format("%s-%s", key, ip);
        }

        int seconds = limiter.seconds();
        int maxCount = limiter.count();

        List<String> keys = Collections.singletonList(key);

        // 因涉及到多线程问题，使用 lua 脚本实现原子化
        // 从 redis 尝试获取 key
        // count++ 或 count = 1
        Long currentCount = 0L;
        try {
            currentCount = redisTemplate.execute(redisScript, keys, maxCount, seconds);
        } catch (Exception e) {
            log.error("在redis细粒度限流中，执行 lua 脚本失败", e);
            throw BusinessException.businessError("服务不可用，请稍后重试");
        }

        // 判断 count > max_count
        return Objects.nonNull(currentCount) && currentCount != 0L && currentCount <= maxCount;
    }
}
