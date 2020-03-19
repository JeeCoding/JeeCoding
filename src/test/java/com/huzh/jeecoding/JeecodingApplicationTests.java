package com.huzh.jeecoding;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class JeecodingApplicationTests {

//    @Autowired
//    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
//        System.out.println(userService.selectAll());

        String value = (String) redisTemplate.opsForValue().get("aaa");
        System.out.println(value);
        redisTemplate.opsForValue().set("aaa", "testaaa");
        value = (String) redisTemplate.opsForValue().get("aaa");
        System.out.println(value);
    }

}
