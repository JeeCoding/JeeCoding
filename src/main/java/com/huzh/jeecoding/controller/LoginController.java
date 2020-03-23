package com.huzh.jeecoding.controller;

import com.huzh.jeecoding.common.CommonConstant;
import com.huzh.jeecoding.common.CommonResult;
import com.huzh.jeecoding.entity.User;
import com.huzh.jeecoding.service.UserService;
import com.huzh.jeecoding.util.JwtTokenUtil;
import com.huzh.jeecoding.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/login")
    public CommonResult login(@RequestBody User loginUser) throws Exception {
        String username = loginUser.getUserName();
        String password = loginUser.getPassWord();

        //情况1：根据用户信息查询，该用户不存在
        User user = userService.getUserByName(username);
        if (user == null) {
            return CommonResult.validateFailed("该用户不存在，请注册");
        }
        //情况2：根据用户信息查询，该用户已注销
        if ("0".equals(user.getState())) {
            return CommonResult.validateFailed("该用户已注销");
        }
        //情况3：根据用户信息查询，该用户已被删除
        if ("1".equals(user.getState())) {
            return CommonResult.validateFailed("该用户已被删除");
        }

        // 生成token
        String token = jwtTokenUtil.generateToken(loginUser);
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        // 设置超时时间
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);

        return CommonResult.success(token,"登录成功");
    }
}
