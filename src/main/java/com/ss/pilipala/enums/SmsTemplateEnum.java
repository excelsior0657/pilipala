package com.ss.pilipala.enums;

import lombok.Getter;

@Getter
public enum SmsTemplateEnum {
    LOGIN_VERIFY_CODE("verify-code", "登录验证码");
    private final String name;
    private final String description;

    SmsTemplateEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
