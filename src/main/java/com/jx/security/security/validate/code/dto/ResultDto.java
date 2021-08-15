package com.jx.security.security.validate.code.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zhangjx
 * @Date: 2020/5/16 22:16
 * @Description:
 */
@Data
@Builder
public class ResultDto {
    private Boolean  success;
    private String msg;
}
