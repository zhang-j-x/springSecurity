package com.jx.security.security.filter;

import com.jx.security.redis.utils.RedisUtil;
import com.jx.security.security.filter.smsAuth.SmsCodeAuthenticationFilter;
import com.jx.security.security.filter.smsAuth.SmsCodeAuthenticationProvider;
import com.jx.security.security.validate.code.filter.ImageValidateCodeFilter;
import com.jx.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @Author: zhangjx
 * @Date: 2020/6/21 17:31
 * @Description:
 */
@Component
public class FilterManageSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    MyUserNamePasswordAuthenticationFilter myUserNamePasswordAuthenticationFilter;
    @Autowired
    HandleSecurityParamFilter handleSecurityParamFilter;
    @Autowired
    SmsCodeAuthenticationFilter smsCodeAuthenticationFilter;

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterAt(myUserNamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(handleSecurityParamFilter, ImageValidateCodeFilter.class);
        http.addFilterBefore(smsCodeAuthenticationFilter, MyUserNamePasswordAuthenticationFilter.class);
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setRedisUtil(redisUtil);
        smsCodeAuthenticationProvider.setUserService(userService);
        http.authenticationProvider(smsCodeAuthenticationProvider);
    }
}
