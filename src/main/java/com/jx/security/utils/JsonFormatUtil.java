package com.jx.security.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author: zhux
 * @date: 2019/7/2 10:57
 * @description: json格式工具
 */
public class JsonFormatUtil {

    public static String toJson(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);

    }
}
