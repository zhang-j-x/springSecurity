package com.jx.security.vo;

import com.jx.security.enums.BusinessRsEnum;
import com.jx.security.enums.IResultCode;
import lombok.Data;

/**
 * @Description: result包装类
 */

@Data
public class Rs<T> {
    private Integer code;
    private String msg;
    private T data;

    public static Rs success(Object data, IResultCode resultEnum){
        Rs rs = new Rs();
        rs.setCode(resultEnum.getCode());
        rs.setMsg(resultEnum.getMsg());
        rs.setData(data);
        return rs;
    }

    public static Rs success(Object data){
        return success(data, BusinessRsEnum.SUCCESS);
    }

    public static Rs success(){
        return success(null, BusinessRsEnum.SUCCESS);
    }

    public static Rs fail(){
        return success(null, BusinessRsEnum.FAIL);
    }

    public static Rs fail(String msg){
        Rs rs = new Rs();
        rs.setCode(BusinessRsEnum.FAIL.getCode());
        rs.setMsg(msg);
        return rs;
    }

    public static Rs fail(IResultCode resultEnum){
        Rs rs = new Rs();
        rs.setCode(resultEnum.getCode());
        rs.setMsg(resultEnum.getMsg());
        return rs;
    }


    public static Rs error(Integer code,String msg){
        Rs rs = new Rs();
        rs.setCode(code);
        rs.setMsg(msg);
        return rs;
    }
}
