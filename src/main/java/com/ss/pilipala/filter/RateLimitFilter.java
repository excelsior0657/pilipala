package com.ss.pilipala.filter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ss.pilipala.config.RateLimitConfig;
import com.ss.pilipala.enums.StandardResponse;
import com.ss.pilipala.response.CommonResponse;
import com.ss.pilipala.utils.JsonUtil;
import com.ss.pilipala.utils.RequestUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于 IP 的粗粒度限流
 * 使用 guava 本地缓存实现
 */
@Component
public class RateLimitFilter implements Filter {
    // 用于记录某个时间区间内访问的次数
    private final Cache<String, Integer> requestCountCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();

    private final RateLimitConfig limitConfig;

    public RateLimitFilter(RateLimitConfig limitConfig) {
        this.limitConfig = limitConfig;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        // 基于 IP 限流
        String ip = RequestUtil.getIpAddress((HttpServletRequest) servletRequest);
        Integer count = requestCountCache.getIfPresent(ip);
        count = Objects.isNull(count)?0:count;
        // 获取每秒最多访问次数
        final Integer maxRequestCount = limitConfig.getMaxRequestCount();
        if(count > maxRequestCount){
            CommonResponse<?> response = new CommonResponse<>(
                    StandardResponse.ERROR.getCode(),
                    "访问过于频繁，请稍后重试",
                    null
            );
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(500);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.getWriter().write(JsonUtil.toJson(response));
        }else{
            requestCountCache.put(ip, count + 1);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
