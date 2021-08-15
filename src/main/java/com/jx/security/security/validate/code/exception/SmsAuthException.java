package com.jx.security.security.validate.code.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author: zhangjx
 * @Date: 2020/7/18 23:56
 * @Description: 手机验证码登陆异常
 */
public class SmsAuthException extends AuthenticationException {
    public SmsAuthException(String msg, Throwable t) {
        super(msg, t);
    }

    public SmsAuthException(String msg) {
        super(msg);
    }
}
