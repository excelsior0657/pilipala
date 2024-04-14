package com.ss.pilipala.config;

import com.apistd.uni.Uni;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Slf4j
@Configuration
@ConfigurationProperties("sms")
public class SmsConfig {
    private String accessKey;
    private String accessSecret;
    private String signature;
    private Map<String, String> templateMapping;

    /**
     * 初始化 SDK
     * <p>
     * 应用启动后即初始化
     */
    @PostConstruct
    public void initSms() {
        Uni.init(accessKey, accessSecret);
        log.info("sms 初始化完成");
    }
}