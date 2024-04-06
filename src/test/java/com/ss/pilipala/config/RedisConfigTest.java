package com.ss.pilipala.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisTemplate(){
        redisTemplate.opsForValue().set("test-key", 13143344);
        System.out.println(redisTemplate.opsForValue().get("test-key"));
    }
}
