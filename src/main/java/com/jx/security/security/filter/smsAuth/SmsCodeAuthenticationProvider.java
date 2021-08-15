package com.jx.security.security.filter.smsAuth;

import com.jx.security.entity.Role;
import com.jx.security.entity.User;
import com.jx.security.enums.LoginRcEnum;
import com.jx.security.redis.utils.RedisUtil;
import com.jx.security.security.validate.code.constant.ValidateCodeConstant;
import com.jx.security.security.validate.code.exception.SmsAuthException;
import com.jx.security.service.IUserService;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

/**
 * @Author: zhangjx
 * @Date: 2020/7/18 23:28
 * @Description: 短信登录验证逻辑
 */

/**
 * 若此处交给Spring管理 手机验证码登陆的Provider能够获取,但是无法获取其他provider
 */
//@Component
@Setter
public class SmsCodeAuthenticationProvider  implements AuthenticationProvider {


    private IUserService userService;

    private RedisUtil redisUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        Object SmsCodeObj = redisUtil.get(ValidateCodeConstant.VALIDATE_SMS_CODE + ValidateCodeConstant.VALIDATE_CODE_REDIS_SEPARATOR + authentication.getPrincipal());
        if(SmsCodeObj == null){
            //登陆验证码已过期
            throw new SmsAuthException(LoginRcEnum.LOGIN_SMS_CODE_OUT_DATE.getMsg());
        }else {
          String smsCode = (String) SmsCodeObj;
          if(!smsCode.equalsIgnoreCase((String) authentication.getCredentials())){
              throw new SmsAuthException(LoginRcEnum.LOGIN_SMS_CODE_ERROR.getMsg());
          }else {
              User user = userService.getUserByPhoneNumber((String) authentication.getPrincipal());
              if (user == null) {
                  throw new SmsAuthException(LoginRcEnum.LOGIN_PHONE_USER_NOT_FOUND.getMsg());
              }
              List<Role> roles = userService.qryUserRoles(user.getId());
              user.setRoles(roles);
              SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user);
              authenticationResult.setDetails(authenticationToken.getDetails());
              return authenticationResult;
          }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
