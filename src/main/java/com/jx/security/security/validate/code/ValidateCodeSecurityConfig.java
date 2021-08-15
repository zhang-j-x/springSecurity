
package com.jx.security.security.validate.code;

import com.jx.security.security.validate.code.filter.ImageValidateCodeFilter;
import com.jx.security.security.validate.code.filter.SmsValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 校验码相关安全配置  把验证码过滤器加到过滤器链里面
 * 
 * @author zhangjx
 *
 */
@Component("validateCodeSecurityConfig")
public class ValidateCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private ImageValidateCodeFilter validateCodeFilter;

    @Autowired
    private SmsValidateCodeFilter smsCodeFilter;
	
	@Override
	public void configure(HttpSecurity http) {
		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
}
