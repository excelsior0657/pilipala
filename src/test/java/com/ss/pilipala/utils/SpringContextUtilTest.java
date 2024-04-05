package com.ss.pilipala.utils;

import com.ss.pilipala.controller.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringContextUtilTest {

    @Test
    public void testGetBean(){
        TestController testController = SpringContextUtil.getBean(TestController.class);
        testController.test();
    }
}
