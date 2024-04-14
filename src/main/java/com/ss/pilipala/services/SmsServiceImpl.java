package com.ss.pilipala.services;

import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import com.ss.pilipala.config.SmsConfig;
import com.ss.pilipala.enums.SmsTemplateEnum;
import com.ss.pilipala.errors.BusinessException;
import com.ss.pilipala.interfaces.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    private final SmsConfig smsConfig;


    public SmsServiceImpl(SmsConfig smsConfig) {
        this.smsConfig = smsConfig;
    }

    @Override
    public void sendCode(String tel, String code, Integer expireMinutes) {
        HashMap<String, String> templateData = new HashMap<>();
        templateData.put("code", code);
        templateData.put("ttl", String.valueOf(expireMinutes));

        UniMessage message = UniSMS.buildMessage()
                .setTo(tel)
                .setSignature(smsConfig.getSignature())
                .setTemplateId(
                        smsConfig.getTemplateMapping()
                                .get(SmsTemplateEnum.LOGIN_VERIFY_CODE.getName())
                )
                .setTemplateData(templateData);
        try {
            UniResponse res = message.send();
            log.info("result: {}", res);
        } catch (Exception e) {
            log.error("send sms error: ", e);
            throw BusinessException.businessError("短信验证码发送失败");
        }
    }
}