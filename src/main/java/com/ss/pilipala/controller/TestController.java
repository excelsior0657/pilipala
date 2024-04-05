package com.ss.pilipala.controller;

import com.ss.pilipala.enums.StandardResponse;
import com.ss.pilipala.errors.BusinessException;
import com.ss.pilipala.utils.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("")
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(HttpServletRequest request){
        String localhost = RequestUtil.getLocalhost();
        String ipAddress = RequestUtil.getIpAddress(request);
        System.out.printf("localhost: %s \n", localhost);
        System.out.printf("client ip: %s \n", ipAddress);

        throw new BusinessException("测试异常信息", StandardResponse.ERROR);
    }

    public void test(){
        System.out.println("这是测试方法");
    }
}
