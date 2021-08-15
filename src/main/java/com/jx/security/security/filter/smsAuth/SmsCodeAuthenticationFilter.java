package com.jx.security.security.filter.smsAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jx.security.entity.User;
import com.jx.security.properties.SecurityProperties;
import com.jx.security.utils.BufferedServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: zhangjx
 * @Date: 2020/7/18 19:55
 * @Description: 手机登陆filter
 */
@Slf4j
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private SessionRegistry sessionRegistry;
    /**
     * 手机验证码登陆地址
     */
    private static final String SMS_LOGIN_URL = "/auth/sms";

    @Autowired
    SecurityProperties securityProperties;

    public SmsCodeAuthenticationFilter() {
        super(SMS_LOGIN_URL);
    }

    protected SmsCodeAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(new AntPathRequestMatcher(SMS_LOGIN_URL));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String phoneNumber = null;
        String smsCode = null;
        // 暂时只有支持 post json 传参实现,
        if(RequestMethod.POST.toString().equals(request.getMethod())
                &&request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)){
            try {
                HttpServletRequest httpRequest = new BufferedServletRequestWrapper(request);
                Map<String,String> map = new ObjectMapper().readValue(httpRequest.getInputStream(),Map.class );
                phoneNumber = map.get(securityProperties.getBrowser().getLoginPhoneNumParamName());
                smsCode = map.get(securityProperties.getBrowser().getLoginSmsCodeParamName());

            } catch (IOException e) {
                log.error("登录操作 获取post参数异常！", e);
                throw new RuntimeException("登录操作 获取post参数异常！" + e.getMessage());
            }
            SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(phoneNumber,smsCode);
            setDetails(request, authRequest);
            User user = new User();
            user.setTel(phoneNumber);
//            sessionRegistry.registerNewSession(request.getSession(true).getId(), user);
            return super.getAuthenticationManager().authenticate(authRequest);
        }else {
            throw new AuthenticationServiceException(
                    "Authentication method " + request.getMethod() + " and Authentication contentType " + request.getContentType() + " not supported. " +
                            "only supported Authentication method:" + RequestMethod.POST + " and Authentication contentType:" +
                            MediaType.APPLICATION_JSON_VALUE);
        }
    }


    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
