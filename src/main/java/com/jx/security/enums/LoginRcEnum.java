package com.jx.security.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @Author: zhangjx
 * @Date: 2020/7/14 17:57
 * @Description:
 */
@AllArgsConstructor
public enum LoginRcEnum implements IResultCode{

    /**
     * 未登录认证
     */
    NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED.value(),"未认证,请先进行登录操作！"),
    /**
     * 踢出已登录用户
     */
    LOGIN_FORCE_OFFLINE(HttpStatus.UNAUTHORIZED.value(),"您已在另一台设备登录，本次登录已下线！"),

    /**
     * 登陆短信验证码已过期
     */
    LOGIN_SMS_CODE_OUT_DATE(HttpStatus.UNAUTHORIZED.value(),"登陆短信验证码已过期，请重新登陆！"),

    /**
     * 登陆短信验证码错误
     */
    LOGIN_SMS_CODE_ERROR(HttpStatus.UNAUTHORIZED.value(),"短信验证码错误！"),
    /**
     * 手机对应用户不存在
     */
    LOGIN_PHONE_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(),"该手机号无对应注册用户！");
    private Integer code;
    private String msg;



    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
