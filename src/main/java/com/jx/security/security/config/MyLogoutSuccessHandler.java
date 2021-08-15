package com.jx.security.security.config;

import com.jx.security.utils.ResponseWriterUtil;
import com.jx.security.vo.Rs;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zhangjx
 * @Date: 2020/7/10 17:19
 * @Description:
 */
@Component
public class MyLogoutSuccessHandler  implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseWriterUtil.writeResponse(response, Rs.success());
    }
}
