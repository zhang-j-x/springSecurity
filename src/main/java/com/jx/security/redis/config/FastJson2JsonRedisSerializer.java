package com.jx.security.redis.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author:zpfms
 * @date:2019-8-22 19:21
 * @description 使用fastjson对redis进行序列化
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 针对fastjson可能由autotype导致反序列化异常进行处理
     */
    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        /*如果遇到反序列化autoType is not support错误，请添加并修改一下包名到bean文件路径
        ParserConfig.getGlobalInstance().addAccept("com.xxxxx.xxx");*/
    }

    private ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> clazz;

    /**
     * 指定序列化类
     *
     * @param clazz
     */
    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 序列化
     *
     * @param t 序列化类
     * @return 序列化后的二进制
     * @throws SerializationException 序列化异常
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t).getBytes(DEFAULT_CHARSET);
    }

    /**
     * 反序列化
     *
     * @param bytes 二进制
     * @return 反序列化后类
     * @throws SerializationException 序列化异常
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz);
    }

    /**
     * 序列化处理映射类设置
     *
     * @param objectMapper 序列化处理映射
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    /**
     * 获取类型
     *
     * @param clazz class
     * @return java类型
     */
    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
}
