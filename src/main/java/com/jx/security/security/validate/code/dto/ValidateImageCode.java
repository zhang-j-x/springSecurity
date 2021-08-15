package com.jx.security.security.validate.code.dto;

import lombok.Data;

/**
 * @Author: zhangjx
 * @Date: 2020/4/11 18:23
 * @Description: 验证码图片信息
 */
@Data
public class ValidateImageCode extends ValidateCode {
    /**base64的图片字符串*/
    private String base64ImageStr;


}
