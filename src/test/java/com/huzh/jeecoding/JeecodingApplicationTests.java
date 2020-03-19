package com.huzh.jeecoding;

import com.huzh.jeecoding.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class JeecodingApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
//        System.out.println(userService.get("1"));
        System.out.println(userService.getUserByName("admin"));

//        String value = (String) redisTemplate.opsForValue().get("aaa");
//        System.out.println(value);
//        redisTemplate.opsForValue().set("aaa", "testaaa");
//        value = (String) redisTemplate.opsForValue().get("aaa");
//        System.out.println(value);
    }

}
