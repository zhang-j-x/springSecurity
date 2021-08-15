package com.jx.security.security.config;


import com.jx.security.dto.LoginLog;
import com.jx.security.entity.User;
import com.jx.security.utils.ResponseWriterUtil;
import com.jx.security.vo.Rs;
import com.jx.security.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: zhangjx
 * @Date: 2020/4/11 00:11
 * @Description: 登录成功的handler
 */
@Slf4j(topic = "login_log")
@Component
public class MyAuthenticationSuccessHandler  extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        //将认证成功信息返回前端
        User principal = (User) authentication.getPrincipal();

        LoginLog loginLog = new LoginLog();
        loginLog.setTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        loginLog.setLoginMethod("UNKNOW");
        loginLog.setResult(true);
        loginLog.setUserName(principal.getUsername());
        log.info(loginLog.toString());

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(principal, userVo);
        ResponseWriterUtil.writeResponse(httpServletResponse, Rs.success(userVo));
    }
}
