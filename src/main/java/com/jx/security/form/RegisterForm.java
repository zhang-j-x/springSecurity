package com.jx.security.form;

import lombok.Data;

/**
 * @Author: zhangjx
 * @Date: 2020/6/14 22:26
 * @Description:
 */
@Data
public class RegisterForm {

    private String username;
    private String password;
    private String smsCode;
    private String smsCodeUuid;
    private String phoneNumber;
}
