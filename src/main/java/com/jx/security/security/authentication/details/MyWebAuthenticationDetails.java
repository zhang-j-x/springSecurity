package com.jx.security.security.authentication.details;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: zhangjx
 * @Date: 2020/6/21 15:59
 * @Description: 自定义WebAuthenticationDetails
 */
public class MyWebAuthenticationDetails extends WebAuthenticationDetails {

    private String loginSuccessTime;

    public static DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public MyWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        LocalDateTime date = LocalDateTime.now();
        loginSuccessTime = DF.format(date);
    }

}


