package com.jx.security.properties;

import lombok.Data;

/**
 * @Author: zhangjx
 * @Date: 2020/5/17 20:46
 * @Description: 短信验证码相关属性
 */
@Data
public class SmsCodeProperties {
    /**验证码字符个数*/
    private Integer codeNum = 4;

    /**验证码的过期时间 单位 秒  默认五分钟*/
    private Integer expireTime = 60 * 5;

    /**前端传过来的电话号码对应参数名*/
    private String phoneNumberParamName = "phoneNumber";
    /**前端传过来的uuid对应参数名*/
    private String uuidParamName = "smsCodeUuid";
    /**前端传过来的验证码对应参数名*/
    private String validateCodeParamName = "smsValidateCode";

    /**需要验证码验证的URL 逗号隔开*/
    private String urls;

}
