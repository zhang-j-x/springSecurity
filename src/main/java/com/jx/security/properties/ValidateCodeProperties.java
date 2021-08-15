package com.jx.security.properties;

import lombok.Data;

/**
 * @Author: zhangjx
 * @Date: 2020/5/16 22:33
 * @Description: 验证码配置类
 */
@Data
public class ValidateCodeProperties {

    private ImageCodeProperties imageCode = new ImageCodeProperties();

    private SmsCodeProperties smsCode = new SmsCodeProperties();
}
