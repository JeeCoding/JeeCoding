package com.huzh.jeecoding;

import com.huzh.jeecoding.common.CommonConstant;
import com.huzh.jeecoding.entity.User;
import com.huzh.jeecoding.service.UserService;
import com.huzh.jeecoding.util.JwtTokenUtil;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void contextLoads() {

//        System.out.println(userService.get("1"));
//        System.out.println(userService.getUserByName("admin"));

        //测试redis
        String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE1ODUwMzA4MTA0MjUsImV4cCI6MTU4NTYzNTYxMH0.YUEr7opQm1xLgBewjdfJNjf5c8AUg0chcvKwo89BRqC1bCSLJQni3akKXrjweG39wwn1N65fKc8Y8ayyNzHqQw";
        String value = (String) redisTemplate.opsForValue().get(CommonConstant.PREFIX_USER_TOKEN + token);
        System.out.println(value);
        redisTemplate.delete(CommonConstant.PREFIX_USER_TOKEN + token);
         value = (String) redisTemplate.opsForValue().get(CommonConstant.PREFIX_USER_TOKEN + token);
        System.out.println(value);
//        redisTemplate.opsForValue().set("aaa", "testaaa1");
//        value = (String) redisTemplate.opsForValue().get("aaa");
//        System.out.println(value);

        //测试jwt
//        User user = new User();
//        user.setUserName("admin");
//        user.setName("管理员");
//        user.setPassWord("123456");
//        String token = jwtTokenUtil.generateToken(user);
//        System.out.println(token);
//        System.out.println(jwtTokenUtil.validateToken(token, user));
    }

}
