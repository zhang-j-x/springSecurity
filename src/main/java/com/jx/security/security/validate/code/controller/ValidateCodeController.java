package com.jx.security.security.validate.code.controller;



import com.jx.security.security.validate.code.dto.ValidateCode;
import com.jx.security.security.validate.code.dto.ValidateImageCode;
import com.jx.security.security.validate.code.generator.ImageCodeGenerator;
import com.jx.security.security.validate.code.generator.SmsCodeGenerator;
import com.jx.security.vo.Rs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhangjx
 * @Date: 2020/4/18 22:11
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/validateCode/")
public class ValidateCodeController {
    @Autowired
    private ImageCodeGenerator imageCodeGenerator;

    @Autowired
    private SmsCodeGenerator smsCodeGenerator;

    /**
     * 获取验证码图片的base64字符串和验证验证码的uuid
     * @param
     * @return: com.jx.core.vo.Rs
     */
    @GetMapping("getImageValidateCode")
    public Rs getImageValidateCode(){
        ValidateImageCode randomImage = (ValidateImageCode) imageCodeGenerator.genValidateCode();
        log.info("获取验证码成功");
        return Rs.success(randomImage);
    }

    /**
     * 发送验证码到手机 并返回uuid
     * @param
     * @return: com.jx.core.vo.Rs
     */
    @GetMapping("getSmsValidateCode")
    public Rs getSmsValidateCode(String phoneNumber){
        ValidateCode validateCode = smsCodeGenerator.genValidateCode();
        return Rs.success(validateCode);
    }
}
