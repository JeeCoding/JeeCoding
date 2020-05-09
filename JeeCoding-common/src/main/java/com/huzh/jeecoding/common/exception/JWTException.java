package com.huzh.jeecoding.common.exception;

/**
 * @author huzh
 * @description:
 * @date 2020/5/8 19:45
 */
public class JWTException extends RuntimeException {

    public JWTException(String message) {
        super(message);
    }

    public JWTException() {
        super();
    }
}
