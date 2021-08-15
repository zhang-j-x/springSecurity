package com.jx.security.security.validate.code.sms;

/**
 * @Author: zhangjx
 * @Date: 2020/5/17 20:27
 * @Description: 短信验证码发送接口
 */
public interface SmsCodeSender {

    /**
     * 发送短信验证码
     * @param phoneNumber 电话号码
     * @param smsCode 验证码
     * @SmsTemplate smsTemplate 短信模板
     */
    public void sendSmsCode(String phoneNumber, String smsCode, SmsTemplate smsTemplate);
}
