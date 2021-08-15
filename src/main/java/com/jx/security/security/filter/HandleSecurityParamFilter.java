package com.jx.security.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jx.security.properties.SecurityProperties;
import com.jx.security.utils.BufferedServletRequestWrapper;
import com.jx.security.utils.ParameterRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangjx
 * @Date: 2020/7/18 15:25
 * @Description:
 */
@Component
public class HandleSecurityParamFilter extends OncePerRequestFilter {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * 登陆请求时将请求body中的rememberMe参数放到request的param中
         * 因为rememberMeService是通过request.getParameter(parameter) 获取前台传过来的是否多少天自动登陆的设置
         * 重写了UsernamePasswordAuthenticationFilter后post传参 那么就无法获取参数，所以后台提前做处理
         */
        if(RequestMethod.POST.toString().equals(request.getMethod())
                && request.getRequestURI().equals(securityProperties.getBrowser().getLoginUrl())){

             request = new BufferedServletRequestWrapper(request);
            Map<String,Object> map = new ObjectMapper().readValue(request.getInputStream(),Map.class );
            Boolean rememberMe = (Boolean) map.get(securityProperties.getBrowser().getLoginRememberMeParamName());

            //把rememberMe 放到request中 使AbstractRememberMeServices 能够通过request.getParameter(parameter)获取参数
            HashMap newParam=new HashMap(request.getParameterMap());
            newParam.put(securityProperties.getBrowser().getLoginRememberMeParamName(),rememberMe);
            ParameterRequestWrapper wrapRequest=new ParameterRequestWrapper(request,newParam);
            request = wrapRequest;
        }
        filterChain.doFilter(request, response);
    }

}
