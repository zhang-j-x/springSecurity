package com.jx.security.security.filter.smsAuth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @Author: zhangjx
 * @Date: 2020/7/18 23:19
 * @Description: 手机验证码登陆信息封装token类
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    private Object credentials;

    public SmsCodeAuthenticationToken(String phoneNumber,String smsCode) {
        super(null);
        this.principal = phoneNumber;
        this.credentials = smsCode;
        setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

}
