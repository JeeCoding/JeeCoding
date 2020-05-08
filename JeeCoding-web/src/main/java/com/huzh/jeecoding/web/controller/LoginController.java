package com.huzh.jeecoding.web.controller;


import com.huzh.jeecoding.common.api.RestResult;
import com.huzh.jeecoding.common.constant.TokenConstant;
import com.huzh.jeecoding.common.util.RedisUtil;
import com.huzh.jeecoding.dao.entity.User;
import com.huzh.jeecoding.security.util.JWTUtil;
import com.huzh.jeecoding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huzh
 */
@Slf4j
@Api(value = "登录Controller")
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JWTUtil jwtUtil;

    @ApiOperation(value = "用户登录", notes = "登录--不进行拦截")
    @PostMapping(value = "/login")
    public RestResult login(@RequestBody User loginUser) throws Exception {
        String username = loginUser.getUserName();
        String password = loginUser.getPassWord();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return RestResult.validateFailed("账号和密码不能为空!");
        }

        //情况1：根据用户信息查询，该用户不存在
        User user = userService.getUserByName(username);
        if (user == null) {
            return RestResult.validateFailed("该用户不存在!");
        }
        if (!password.equals(user.getPassWord())) {
            throw new UnknownAccountException("用户名和密码错误!");
        }
        //情况2：根据用户信息查询，该用户已注销
        if ("0".equals(user.getState())) {
            return RestResult.validateFailed("该用户已注销!");
        }
        //情况3：根据用户信息查询，该用户已被删除
        if ("1".equals(user.getState())) {
            return RestResult.validateFailed("该用户已被删除!");
        }

        // 生成token
        String token = jwtUtil.generateToken(username);
        redisUtil.set(TokenConstant.PREFIX_USER_TOKEN + token, token);
        // 设置超时时间
        redisUtil.expire(TokenConstant.PREFIX_USER_TOKEN + token, jwtUtil.getExpiration() * 1000);

        return RestResult.success(token, "登录成功");
    }
}
