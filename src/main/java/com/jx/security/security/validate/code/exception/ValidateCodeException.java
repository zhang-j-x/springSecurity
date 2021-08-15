package com.jx.security.security.validate.code.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @Author: zhangjx
 * @Date: 2020/4/18 22:18
 * @Description:
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
