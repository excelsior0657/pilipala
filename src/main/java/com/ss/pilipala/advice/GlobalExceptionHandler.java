package com.ss.pilipala.advice;

import com.ss.pilipala.enums.StandardResponse;
import com.ss.pilipala.errors.BusinessException;
import com.ss.pilipala.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private void logError(String message, Throwable e){
        log.error(message);
        if(log.isDebugEnabled()){
            log.debug("异常堆栈信息", e);
        }
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleBusinessException(BusinessException e){
        logError(e.getMessage(), e);
        return new CommonResponse<>(StandardResponse.ERROR.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e){
        logError("SSE 连接超时", e);
        return new CommonResponse<>(StandardResponse.ERROR.getCode(), "SSE 异步请求超时", null);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleRuntimeException(RuntimeException e){
        logError(e.getMessage(), e);
        return new CommonResponse<>(StandardResponse.ERROR.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object handleException(Exception e){
        logError(e.getMessage(), e);
        return new CommonResponse<>(StandardResponse.ERROR.getCode(), e.getMessage(), null);
    }
}
