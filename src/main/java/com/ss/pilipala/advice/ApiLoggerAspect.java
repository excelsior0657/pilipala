package com.ss.pilipala.advice;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ss.pilipala.utils.JsonUtil;
import com.ss.pilipala.utils.RequestUtil;
import com.ss.pilipala.utils.models.ExceptionInfo;
import com.ss.pilipala.utils.models.LogModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class ApiLoggerAspect {
    private final HttpServletRequest request;

    public ApiLoggerAspect(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * controller包下的所有方法
     */
    @Pointcut("execution(* com.ss.pilipala.controller..*Controller.*(..))")
    public void apiLog() {
    }

    @Around("apiLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        LogModel logModel = new LogModel();
        long start = System.currentTimeMillis();
        result = point.proceed();
        // 解析请求
        parseRequest(point, logModel);
        // 设置返回值
        //logModel.setResponse(result instanceof CommonResponse ? result : "NoSerializationRequired");
        long end = System.currentTimeMillis();
        logModel.setResponse(result)
                .setTimestamp(start)
                .setCost(end - start);
        String json = JsonUtil.toJson(logModel);
        log.info(json);

        return result;
    }

    private void parseRequest(JoinPoint point, LogModel logModel) {
        String ip = RequestUtil.getIpAddress(request);
        Object[] args = point.getArgs();
        CodeSignature signature = (CodeSignature) point.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], Objects.toString(args[i]));
        }

        logModel
                .setIp(ip)
                .setMethod(request.getMethod())
                .setPath(request.getRequestURL().toString())
                .setParams(params);
    }

    @AfterThrowing(value = "apiLog()", throwing = "e")
    public void afterThrowing(JoinPoint point, Throwable e) throws JsonProcessingException {
        LogModel logModel = new LogModel();
        long start = System.currentTimeMillis();
        // 解析请求
        parseRequest(point, logModel);
        // 解析异常信息
        ExceptionInfo exceptionInfo = parseExceptionInfo(e);
        long end = System.currentTimeMillis();
        logModel.setException(exceptionInfo)
                .setTimestamp(start)
                .setCost(end - start);
        String json = JsonUtil.toJson(logModel);
        log.info(json);
    }

    private ExceptionInfo parseExceptionInfo(Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        String stackTracing = Arrays.toString(stackTrace)
                .replace("[", "")
                .replace("]", "");
        StackTraceElement topTraceElement = stackTrace[0];

        ExceptionInfo exceptionInfo = new ExceptionInfo();
        exceptionInfo.setMessage(e.getMessage())
                .setFilename(topTraceElement.getFileName())
                .setClassName(topTraceElement.getClassName())
                .setMethodName(topTraceElement.getMethodName())
                .setLineNumber(topTraceElement.getLineNumber())
                .setDetails(stackTracing);
        return exceptionInfo;
    }
}
