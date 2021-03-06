package com.jx.security.security.config;


import com.jx.security.enums.LoginRcEnum;
import com.jx.security.properties.SecurityProperties;
import com.jx.security.security.authentication.details.MyWebAuthenticationDetailsSource;
import com.jx.security.security.filter.FilterManageSecurityConfig;
import com.jx.security.security.filter.MyUserNamePasswordAuthenticationFilter;
import com.jx.security.security.filter.smsAuth.SmsCodeAuthenticationFilter;
import com.jx.security.security.validate.code.ValidateCodeSecurityConfig;
import com.jx.security.utils.ResponseWriterUtil;
import com.jx.security.vo.Rs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zhangjx
 * @Date: 2020/4/3 17:33
 * @Description:
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Qualifier("myUserDetailService")
    @Autowired
    private UserDetailsService myUserDetailService;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private MyWebAuthenticationDetailsSource myWebAuthenticationDetailsSource;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private FilterManageSecurityConfig filterManageSecurityConfig;



    /**
     * ?????????UsernamePasswordAuthenticationFilter????????????????????????????????????????????????
     * @return
     * @throws Exception
     */
    @Bean
    public MyUserNamePasswordAuthenticationFilter loginFilter() throws Exception {
        MyUserNamePasswordAuthenticationFilter userNamePasswordAuthenticationFilter = new MyUserNamePasswordAuthenticationFilter();
        userNamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        userNamePasswordAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        userNamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        userNamePasswordAuthenticationFilter.setFilterProcessesUrl(securityProperties.getBrowser().getLoginUrl());
        userNamePasswordAuthenticationFilter.setUsernameParameter(securityProperties.getBrowser().getLoginUserNameParamName());
        userNamePasswordAuthenticationFilter.setPasswordParameter(securityProperties.getBrowser().getLoginUserPasswordParamName());
        userNamePasswordAuthenticationFilter.setAuthenticationDetailsSource(myWebAuthenticationDetailsSource);
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        userNamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy);
        userNamePasswordAuthenticationFilter.setRememberMeServices(persistentTokenBasedRememberMeServices());
        return userNamePasswordAuthenticationFilter;
    }

    @Bean
    public SmsCodeAuthenticationFilter smsCodeAuthenticationFilter() throws Exception {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        smsCodeAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        smsCodeAuthenticationFilter.setAuthenticationDetailsSource(myWebAuthenticationDetailsSource);
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        smsCodeAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy);
        smsCodeAuthenticationFilter.setRememberMeServices(persistentTokenBasedRememberMeServices());
        return smsCodeAuthenticationFilter;
    }

    /**
     * ??????????????? token???????????????
     * @return
     */
    @Bean
    PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(){
        /**
         * key??????????????? ???????????????key?????????????????????????????????uuid ,????????????????????????????????????????????????cookie??????
         * ?????????????????????????????????key?????????????????????????????????????????????????????????????????????
         * ?????????key???RememberMeAuthenticationFilter ???key???????????? ??????????????????????????????????????????????????????key???
         */
        PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices =
                new PersistentTokenBasedRememberMeServices("security",myUserDetailService,persistentTokenRepository);
        persistentTokenBasedRememberMeServices.setTokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds());
        persistentTokenBasedRememberMeServices.setParameter(securityProperties.getBrowser().getLoginRememberMeParamName());
       //??????????????????????????????????????????????????????????????????????????? true?????????????????????????????? ???????????????cookie?????????token
        persistentTokenBasedRememberMeServices.setAlwaysRemember(false);
       return  persistentTokenBasedRememberMeServices;
    }

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * ????????????ConcurrentSessionFilter ???session????????????
     * @return
     */
    @Bean
    ConcurrentSessionFilter concurrentSessionFilter(){
        SessionInformationExpiredStrategy sessionInformationExpiredStrategy = (event) -> {
                HttpServletResponse resp = event.getResponse();
                resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                ResponseWriterUtil.writeResponse(resp, Rs.fail(LoginRcEnum.LOGIN_FORCE_OFFLINE));
        };
        ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry(),sessionInformationExpiredStrategy);
        return concurrentSessionFilter;
    }

    /**
     * ??????????????? session ???????????????????????????????????? session ???????????????????????????????????????????????????
     * ?????????????????? session?????????????????????????????????session ???????????????????????????????????????????????????
     * StandardSession#invalidate ??????????????????????????????????????????????????? Spring ??????????????????
     * ??????????????????????????????????????????Spring Security ?????????????????????????????????
     *
     * ???????????? HttpSessionEventPublisher ????????????????????? HttpSessionListener ?????????
     * ?????? Bean ??????????????? session ?????????????????????????????????????????????
     * ???????????? Spring ??????????????????????????????????????????????????????????????????????????? Spring Security ?????????
     * @return
     */
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("==============?????????WebSecurityConfigurerAdapter========");
        http
            .authorizeRequests()
            .antMatchers("/validateCode/*","/user/*","/auth/sms").permitAll()
            .anyRequest().authenticated()
            .and()
            .apply(validateCodeSecurityConfig).and().apply(filterManageSecurityConfig)
            .and()
            .addFilterAt(concurrentSessionFilter(), ConcurrentSessionFilter.class)
            .formLogin()
                .loginPage("/authentication/require")
                .permitAll()
                .and()
            .logout()
                .logoutUrl(securityProperties.getBrowser().getLogoutUrl())
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                /*
                 * .rememberMe() ???????????????RememberMeAuthenticationFilter
                 *  .rememberMeServices ??????????????? RememberMeAuthenticationFilter ?????????persistentTokenBasedRememberMeServices
                 *  ??????cookie???rememberme ????????????????????????????????????
                 */
                .rememberMe()
                .rememberMeServices(persistentTokenBasedRememberMeServices())
                .key("security")
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .and()
            .csrf().disable()
            .sessionManagement().maximumSessions(1);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //????????????????????????????????????
        web.ignoring().antMatchers("/static/**");
    }


}
