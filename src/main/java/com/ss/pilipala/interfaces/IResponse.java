package com.ss.pilipala.interfaces;

public interface IResponse {
    /**
     * 获取自定义状态码
     *
     * @return 状态码
     */
    int getCode();

    /**
     * 获取消息
     *
     * @return 消息
     */
    String getMessage();
}
