package com.ss.pilipala.interfaces;

public interface SmsService {
    void sendCode(String tel, String code, Integer expireMinutes);
}
