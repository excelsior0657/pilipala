package com.ss.pilipala.errors;

import com.ss.pilipala.enums.StandardResponse;
import com.ss.pilipala.interfaces.IResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException implements IResponse {
    private final int code;
    private final String message;
    private final IResponse response;

    public BusinessException(IResponse response) {
        this.code = response.getCode();
        this.message = response.getMessage();
        this.response = response;
    }

    public BusinessException(String message, IResponse response) {
        this.code = response.getCode();
        this.message = StringUtils.isBlank(message) ? response.getMessage() : message;
        this.response = response;
    }

    public BusinessException(String message, Object[] args, IResponse response) {
        super(StringUtils.isBlank(message) ? response.getMessage() : message);
        this.code = response.getCode();
        this.message = MessageFormat.format(super.getMessage(), args);
        this.response = response;
    }

    public static BusinessException businessError(String msg){
        return new BusinessException(msg, StandardResponse.ERROR);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
