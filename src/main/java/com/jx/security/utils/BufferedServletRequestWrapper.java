package com.jx.security.utils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: zhangjx
 * @Date: 2020/6/27 14:16
 * @Description: 解决request.getinputstream() 流重复读取问题
 */
public class BufferedServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] buffer;

    public BufferedServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is = request.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] tmp = new byte[1024];
        int read;
        while ((read = is.read(tmp)) > 0) {
            os.write(tmp, 0, read);
        }
        this.buffer = os.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new BufferedServletInputStream(this.buffer);
    }
}