package com.jx.security.utils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @Author: zhangjx
 * @Date: 2020/6/27 14:17
 * @Description: 解决request.getinputstream() 流重复读取问题
 */
public class BufferedServletInputStream extends ServletInputStream {
    private ByteArrayInputStream inputStream;

    public BufferedServletInputStream(byte[] buffer) {
        this.inputStream = new ByteArrayInputStream(buffer);
    }

    @Override
    public int available() {
        return inputStream.available();
    }

    @Override
    public int read() {
        return inputStream.read();
    }

    @Override
    public boolean isFinished() {
        return inputStream.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new RuntimeException("Not implemented");
    }
}
