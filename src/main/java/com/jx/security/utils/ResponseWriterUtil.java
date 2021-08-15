package com.jx.security.utils;

import com.alibaba.fastjson.JSON;

import com.jx.security.constants.HttpConstant;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zhangjx
 * @Date: 2020/4/11 00:15
 * @Description: 输出流
 */
public class ResponseWriterUtil {

    /**
     *
     * @param response
     * @param data 回写数据
     * @return: void
     */
    public static void writeResponse(HttpServletResponse response, Object data){
        response.setContentType(HttpConstant.JSON_CONTENT_TYPE);
        try {
            response.getWriter().write(JSON.toJSONString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param response
     * @param data 回写数据
     * @param httpStatus 服务状态码
     * @return: void
     */
    public static void writeResponse(HttpServletResponse response, Object data, HttpStatus httpStatus){
        response.setContentType(HttpConstant.JSON_CONTENT_TYPE);
        try {
            response.setStatus(httpStatus.value());
            response.getWriter().write(JSON.toJSONString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
