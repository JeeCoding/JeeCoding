package com.huzh.jeecoding;

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
        System.out.println(userService.get("1"));
        System.out.println(userService.getUserByName("admin"));

        //测试redis
//        String value = (String) redisTemplate.opsForValue().get("aaa");
//        System.out.println(value);
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
