package com.jx.security.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jx.security.entity.User;
import com.jx.security.properties.SecurityProperties;
import com.jx.security.utils.BufferedServletRequestWrapper;
import com.jx.security.utils.RsaEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: zhangjx
 * @Date: 2020/6/21 16:39
 * @Description:
 */
@Slf4j
public class MyUserNamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private RsaEncryptUtil rsaEncryptUtil;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String username = null;
        String password = null;
        String  imageUUid = null;
        // 暂时只有支持 post json 传参实现,
        if(RequestMethod.POST.toString().equals(request.getMethod())
                &&request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)){
            try {
                HttpServletRequest httpRequest = new BufferedServletRequestWrapper(request);
                Map<String,String> map = new ObjectMapper().readValue(httpRequest.getInputStream(),Map.class );
                username = map.get(securityProperties.getBrowser().getLoginUserNameParamName());
                String rsaPassword = map.get(securityProperties.getBrowser().getLoginUserPasswordParamName());
                password = rsaEncryptUtil.decrypt(rsaPassword,rsaEncryptUtil.getPrivateKey());
                imageUUid = map.get(securityProperties.getValidateCode().getImageCode().getUuidParamName());
            } catch (IOException e) {
                log.error("登录操作 获取post参数异常！", e);
                throw new RuntimeException("登录操作 获取post参数异常！" + e.getMessage());
            }catch (Exception e) {
                log.error("登录操作 密码解密异常！", e);
                throw new BadCredentialsException("密码不符合加密规则！请在指定登陆页面登陆。");
            }
            String exceptPwdEnd =  imageUUid.substring(9,13);
            String realPwdEnd = password.substring(password.length() - 4,password.length());
            if(!exceptPwdEnd.equals(realPwdEnd)){
                throw new BadCredentialsException("有抓包登陆风险，请在指定页面登陆！");
            }
            password = password.substring(0,password.length() - 4);
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, password);
            setDetails(request, authRequest);
            User user = new User();
            user.setUsername(username);
            sessionRegistry.registerNewSession(request.getSession(true).getId(), user);
            return super.getAuthenticationManager().authenticate(authRequest);
        }else {
            throw new AuthenticationServiceException(
                    "Authentication method " + request.getMethod() + " and Authentication contentType " + request.getContentType() + " not supported. " +
                            "only supported Authentication method:" + RequestMethod.POST + " and Authentication contentType:" +
                            MediaType.APPLICATION_JSON_VALUE);
        }
    }


}
