package com.jx.security.properties;


import lombok.Data;

/**
 * @Author: zhangjx
 * @Date: 2020/4/10 23:44
 * @Description: security browser相关配置
 */
@Data
public class BrowserProperties {
    /**
     * 默认账号密码登陆url
     */
    private String loginUrl = "/auth/login";

    private String smsLoginUrl = "/auth/sms";

    private String logoutUrl = "/logout";

    /**记住我免登陆秒数*/
    private Integer rememberMeSeconds = 24 * 3600 * 7;

    /**============================账号密码登陆=================================*/
    private String loginUserNameParamName = "userName";

    private String loginUserPasswordParamName = "password";

    private String loginRememberMeParamName = "rememberMe";

    /**=========================手机验证码登陆==================================*/

    private String loginPhoneNumParamName = "phoneNumber";

    private String loginSmsCodeParamName = "smsCode";


    /**
     * 记住我 在登陆成功后写入cookie token对应的参数名
     */
    private String loginRememberMeCookieName = "SecurityRemember";

    /**
     * rememberService 和 RememberMeAuthenticationFilter 中的Key
     */
    private String loginRememberMeServiceKey = "security";


}
