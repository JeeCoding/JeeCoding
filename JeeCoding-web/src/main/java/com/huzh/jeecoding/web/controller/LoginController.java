package com.huzh.jeecoding.web.controller;


import com.huzh.jeecoding.common.api.RestResult;
import com.huzh.jeecoding.common.constant.RedisConstant;
import com.huzh.jeecoding.common.exception.UnauthorizedException;
import com.huzh.jeecoding.common.util.JWTUtil;
import com.huzh.jeecoding.common.util.PasswordUtil;
import com.huzh.jeecoding.common.util.redis.RedisUtil;
import com.huzh.jeecoding.dao.entity.User;
import com.huzh.jeecoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author huzh
 */
@Slf4j
@Api(value = "登录Controller")
@RestController
public class LoginController {

    @Value("${config.refreshToken-expireTime}")
    private String refreshTokenExpireTime;

    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户登录", notes = "登录--不进行拦截")
    @PostMapping(value = "/login")
    public RestResult login(@RequestBody User loginUser, HttpServletResponse response) throws Exception {
        String username = loginUser.getUserName();
        String password = loginUser.getPassWord();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return RestResult.validateFailed("账号和密码不能为空!");
        }

        //情况1：根据用户信息查询，该用户不存在
        User user = userService.getUserByName(username);
        if (user == null) {
            throw new UnauthorizedException("该帐号不存在(The account does not exist.)");
        }

        System.out.println(PasswordUtil.getEncryptedPwd(password));
        if (PasswordUtil.validPasswd(password, user.getPassWord())) {
            // 清除可能存在的Shiro权限信息缓存
            if (RedisUtil.exists(RedisConstant.PREFIX_SHIRO_CACHE + username)) {
                RedisUtil.delete(RedisConstant.PREFIX_SHIRO_CACHE + username);
            }
            // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            RedisUtil.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + username, currentTimeMillis,
                    Integer.parseInt(refreshTokenExpireTime));
            // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
            String token = JWTUtil.createToken(username, currentTimeMillis);
            response.setHeader("Authorization", token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            return RestResult.success(token, "登录成功(Login Success.)");
        } else {
            throw new UnauthorizedException("帐号或密码错误(Account or Password Error.)");
        }
    }
}
