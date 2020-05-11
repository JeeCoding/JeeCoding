package com.huzh.jeecoding.common.exception;

import com.huzh.jeecoding.common.api.RestResult;
import com.huzh.jeecoding.common.api.ResultCode;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huzh
 * @description: 异常处理
 * @date 2020/5/7 20:28
 */
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 捕捉所有Shiro异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public RestResult handle401(ShiroException e) {
        return RestResult.failed(ResultCode.UNAUTHORIZED, "无权访问(Unauthorized):" + e.getMessage());
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public RestResult handle401(UnauthorizedException e) {
        return RestResult.failed(ResultCode.UNAUTHORIZED, "无权访问(Unauthorized):当前Subject没有此请求所需权限(" + e.getMessage() + ")");
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public RestResult handle401(UnauthenticatedException e) {
        return RestResult.failed(ResultCode.UNAUTHORIZED, "无权访问(Unauthorized):当前Subject是匿名Subject，请先登录(This subject is anonymous.)");
    }

    /**
     * 捕捉校验异常(BindException)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public RestResult validException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> error = this.getValidError(fieldErrors);
        return RestResult.failed(ResultCode.VALIDATE_FAILED, error.get("errorMsg").toString());
    }

    /**
     * 捕捉校验异常(MethodArgumentNotValidException)
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResult validException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> error = this.getValidError(fieldErrors);
        return RestResult.failed(ResultCode.VALIDATE_FAILED, error.get("errorMsg").toString());
    }

    /**
     * 捕捉404异常
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public RestResult handle(NoHandlerFoundException e) {
        return RestResult.failed(ResultCode.VALIDATE_FAILED, e.getMessage());
    }

    /**
     * 捕捉其他所有异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestResult globalException(HttpServletRequest request, Throwable ex) {
        return RestResult.failed(ex.toString() + ": " + ex.getMessage());
    }

    /**
     * 捕捉其他所有自定义异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public RestResult handle(CustomException e) {
        return RestResult.failed(e.getMessage());
    }

    /**
     * 获取状态码
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 获取校验错误信息
     */
    private Map<String, Object> getValidError(List<FieldError> fieldErrors) {
        Map<String, Object> map = new HashMap<String, Object>(16);
        List<String> errorList = new ArrayList<String>();
        StringBuffer errorMsg = new StringBuffer("校验异常(ValidException):");
        for (FieldError error : fieldErrors) {
            errorList.add(error.getField() + "-" + error.getDefaultMessage());
            errorMsg.append(error.getField() + "-" + error.getDefaultMessage() + ".");
        }
        map.put("errorList", errorList);
        map.put("errorMsg", errorMsg);
        return map;
    }

}