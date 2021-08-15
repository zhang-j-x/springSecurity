package com.jx.security.security.validate.code.sms;

import org.springframework.stereotype.Component;

/**
 * @Author: zhangjx
 * @Date: 2020/5/17 20:34
 * @Description:
 */
@Component
public class DefaultSmsTemplate  implements SmsTemplate{

    @Override
    public String getSmsTemplate(String smsCode) {
        return "【哔哩哔哩】" + smsCode + "短信登录验证码,五分钟内有效，请勿泄露。";
    }

}
