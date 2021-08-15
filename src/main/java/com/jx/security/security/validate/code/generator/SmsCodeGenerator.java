package com.jx.security.security.validate.code.generator;


import com.jx.security.properties.SecurityProperties;
import com.jx.security.security.validate.code.constant.ValidateCodeConstant;
import com.jx.security.security.validate.code.dto.ResultDto;
import com.jx.security.security.validate.code.dto.ValidateCode;
import com.jx.security.security.validate.code.sms.SmsCodeSender;
import com.jx.security.security.validate.code.sms.SmsTemplate;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author: zhangjx
 * @Date: 2020/5/17 19:28
 * @Description: 短信验证码生成器
 */
@Component
public class SmsCodeGenerator extends AbstractValidateCodeGenerator {

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode genValidateCode() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String phoneNumber = request.getParameter(securityProperties.getValidateCode().getSmsCode().getPhoneNumberParamName());
        String smsCode = getSmsCode();
        ValidateCode validateCode = new ValidateCode();
        validateCode.setUuid(phoneNumber);
        //发送短信验证码
        smsCodeSender.sendSmsCode(phoneNumber, smsCode, smsTemplate);
        //向redis存入验证码
        getRedisUtil().set(
                ValidateCodeConstant.VALIDATE_SMS_CODE + ValidateCodeConstant.VALIDATE_CODE_REDIS_SEPARATOR + phoneNumber,
                smsCode,securityProperties.getValidateCode().getSmsCode().getExpireTime());
        return validateCode;
    }



    @Override
    public ResultDto judgeValidateCodeRight(String uuid, String validateCode) {
        return null;
    }

    private String getSmsCode() {
        return RandomStringUtils.randomNumeric(securityProperties.getValidateCode().getSmsCode().getCodeNum());
    }


}
