package com.jx.security.security.validate.code.sms;

import org.springframework.stereotype.Component;

/**
 * @Author: zhangjx
 * @Date: 2020/5/17 20:31
 * @Description:
 */
@Component
public class DefaultSmsSender implements SmsCodeSender {

    @Override
    public void sendSmsCode(String phoneNumber, String smsCode, SmsTemplate smsTemplate) {
        System.out.println("用户" + phoneNumber + "发送验证码:" + smsTemplate.getSmsTemplate(smsCode));
    }
}
