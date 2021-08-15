package com.jx.security.properties;

import lombok.Data;

/**
 * @Author: zhangjx
 * @Date: 2020/5/16 22:38
 * @Description: 图片验证码生成逻辑
 */
@Data
public class ImageCodeProperties {
    /**图片宽度*/
    private Integer imgWidth=100;
    /**图片高度*/
    private Integer imgHeight = 30;
    /**验证码字符个数*/
    private Integer codeNum = 4;
    /**干扰线数量*/
    private Integer lineCount = 50;
    /**验证码的过期时间 单位 秒  默认五分钟*/
    private Integer expireTime = 60 * 5;

    /**需要验证码验证的URL 逗号隔开*/
    private String urls;


    /**前端传过来的uuid对应参数名*/
    private String uuidParamName = "imageCodeUuid";
    /**前端传过来的验证码对应参数名*/
    private String validateCodeParamName = "imageValidateCode";
}
