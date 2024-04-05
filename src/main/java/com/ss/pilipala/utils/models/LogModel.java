package com.ss.pilipala.utils.models;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 日志模型
 * 记录日志信息
 */
@Data
@Accessors(chain = true)
public class LogModel {
    private String ip;
    private String method;
    private String path;
    private Map<String, Object> params;
    private Object response;
    private ExceptionInfo exception;
    private Long timestamp;
    private Long cost;
}
