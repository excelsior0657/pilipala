package com.ss.pilipala.response;

import com.ss.pilipala.enums.StandardResponse;
import com.ss.pilipala.interfaces.IResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse<T> implements IResponse {
    private final int code;
    private final String message;
    private final T data;

    public static <T> CommonResponse<T> success(T data){
        return new CommonResponse<>(StandardResponse.OK.getCode(), StandardResponse.OK.getMessage(), data);
    }

    public static <T>  CommonResponse<T> success(){
        return success(null);
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
