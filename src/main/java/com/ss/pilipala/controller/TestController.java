package com.ss.pilipala.controller;

import com.ss.pilipala.annotaions.RateLimiter;
import com.ss.pilipala.enums.LimitType;
import com.ss.pilipala.enums.StandardResponse;
import com.ss.pilipala.errors.BusinessException;
import com.ss.pilipala.interfaces.SmsService;
import com.ss.pilipala.utils.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("")
@Tag(name = "测试控制类")
@RestController
public class TestController {
    @Autowired
    private SmsService smsService;

    @GetMapping("/test")
    @Operation(summary = "这是一个测试接口")
    @RateLimiter(key = "limit:test", seconds = 6, count = 2, message = "测试-请求频繁", limitType = LimitType.IP)
    public String test(HttpServletRequest request){
        String localhost = RequestUtil.getLocalhost();
        String ipAddress = RequestUtil.getIpAddress(request);
        System.out.printf("localhost: %s \n", localhost);
        System.out.printf("client ip: %s \n", ipAddress);

        // throw new BusinessException("测试异常信息", StandardResponse.ERROR);
        return "哦哈呦";
    }

    public void test(){
        System.out.println("这是测试方法");
    }

    @GetMapping("/sms")
    public void testSms(){
        smsService.sendCode("17338676087", "8848", 30);
    }
}
