package com.ss.pilipala.utils;

import com.ss.pilipala.errors.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
        log.info("SpringContext 注入完毕");
    }

    private static void applicationContextExists(){
        if(Objects.isNull(applicationContext)){
            throw BusinessException.businessError("SpringContext 丢失");
        }
    }

    public static <T> T getBean(Class<T> clazz){
        applicationContextExists();
        return applicationContext.getBean(clazz);
    }
}
