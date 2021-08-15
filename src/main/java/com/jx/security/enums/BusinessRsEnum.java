package com.jx.security.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BusinessRsEnum implements IResultCode{

    /**
     *
     */
    SUCCESS(0,"操作成功"),
    /**
     *
     */
    FAIL(-1,"操作失败"),


    /**
     *
     */
    INNER_ERROR(-2,"服务器异常，请联系管理员！");

    private Integer code;
    private String msg;


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
