package com.huzh.jeecoding.common.exception;

import com.huzh.jeecoding.common.api.RestResult;
import com.huzh.jeecoding.common.api.ResultCode;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author huzh
 * @description: 异常处理
 * @date 2020/5/7 20:28
 */
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class})
    public RestResult unauthenticatedException() {
        return RestResult.failed(ResultCode.UNAUTHORIZED, "对不起,您还未登录!");
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public RestResult unauthorizedException() {
        return RestResult.failed(ResultCode.FORBIDDEN, "对不起,您没权限操作!");
    }

    @ExceptionHandler(UnknownAccountException.class)
    @ResponseBody
    public RestResult unknownAccountException() {
        return RestResult.failed("登陆失败,用户名或密码错误!");
    }
}