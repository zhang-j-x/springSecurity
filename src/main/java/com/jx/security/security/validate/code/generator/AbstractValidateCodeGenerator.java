package com.jx.security.security.validate.code.generator;


import com.jx.security.redis.utils.RedisUtil;
import com.jx.security.security.validate.code.constant.ValidateCodeConstant;
import com.jx.security.security.validate.code.dto.ResultDto;
import com.jx.security.security.validate.code.dto.ValidateCode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zhangjx
 * @Date: 2020/5/17 19:51
 * @Description: 验证码生成器接口
 */
@Data
public abstract class AbstractValidateCodeGenerator {

    @Autowired
    private RedisUtil redisUtil;
    /**
     * 生成验证码
     * @description:
     * @param
     * @return: com.jx.auth.core.validate.code.dto.ValidateCode
     */
    public  abstract ValidateCode genValidateCode();

    /**
     * 判断验证码是否正确
     * @param uuid
     * @param validateCode
     * @return: java.lang.Boolean
     */
    public ResultDto judgeValidateCodeRight(String uuid, String validateCode){
        //如果验证码没过期
        if (redisUtil.hasKey(ValidateCodeConstant.VALIDATE_IMAGE_CODE + ValidateCodeConstant.VALIDATE_CODE_REDIS_SEPARATOR + uuid)){
            String targetValidateCode = (String) redisUtil.get(ValidateCodeConstant.VALIDATE_IMAGE_CODE + ValidateCodeConstant.VALIDATE_CODE_REDIS_SEPARATOR + uuid);
            Boolean success = validateCode.equalsIgnoreCase(targetValidateCode);
            //如果success字段为true则不管 false抛异常提示取msg字段
            return ResultDto.builder().success(success).msg(ValidateCodeConstant.VALIDATE_CODE_ERROR).build();
        }else{
            return ResultDto.builder().success(false).msg(ValidateCodeConstant.VALIDATE_CODE_OUT_TIME).build();
        }
    }
}
