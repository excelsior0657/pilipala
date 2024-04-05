package com.ss.pilipala.utils.models;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 异常信息
 */
@Data
@Accessors(chain = true)
public class ExceptionInfo {
    private String message;
    private String filename;
    private String className;
    private String methodName;
    private Integer lineNumber;
    private Object details;
}
