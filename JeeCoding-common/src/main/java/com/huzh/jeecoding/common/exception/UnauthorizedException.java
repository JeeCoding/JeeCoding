package com.huzh.jeecoding.common.exception;

/**
 * 自定义401无权限异常(UnauthorizedException)
 * @author dolyw.com
 * @date 2018/8/30 13:59
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String msg){
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }
}
