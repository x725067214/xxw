package com.xxw.exception;

/**
 * 业务异常
 *
 * @author xxw
 * @date 2018/8/8
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
