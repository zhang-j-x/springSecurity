package com.jx.security.security.validate.code.sms;

/**
 * @Author: zhangjx
 * @Date: 2020/5/17 20:29
 * @Description: 短信模板
 */
public interface SmsTemplate {
    /**
     * 获取短信模板
     * @description:
     * @return:
     */
    public String getSmsTemplate(String smsCode);
}
