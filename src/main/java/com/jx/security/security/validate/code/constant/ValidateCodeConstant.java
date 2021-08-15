package com.jx.security.security.validate.code.constant;

/**
 * @Author: zhangjx
 * @Date: 2020/5/16 22:18
 * @Description:
 */
public class ValidateCodeConstant {
    public static final String VALIDATE_CODE_MUST_NOT_NULL = "验证码不能为空";

    public static final String VALIDATE_CODE_ERROR = "验证码错误";

    public static final String VALIDATE_CODE_OUT_TIME = "验证码已过期,请刷新再试！";

    /**图形验证码 验证码存入redis的前缀*/
    public static final String VALIDATE_IMAGE_CODE = "VALIDATE_IMAGE_CODE";
    /***短信验证码 验证码存入redis的前缀*/
    public static final String VALIDATE_SMS_CODE = "VALIDATE_SMS_CODE";
    /**验证码存入redis的分隔符*/
    public static final String VALIDATE_CODE_REDIS_SEPARATOR = "#";

}
