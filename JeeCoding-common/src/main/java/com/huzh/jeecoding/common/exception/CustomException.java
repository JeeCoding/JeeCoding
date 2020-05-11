package com.huzh.jeecoding.common.exception;

/**
 * @author huzh
 * @description:
 * @date 2020/5/8 19:45
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }

    public CustomException() {
        super();
    }
}
