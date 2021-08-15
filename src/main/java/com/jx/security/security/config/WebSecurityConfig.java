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
     * 自定义UsernamePasswordAuthenticationFilter后很多属性注入无效，需要手动注入
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
     * 记住我功能 token持久化实现
     * @return
     */
    @Bean
    PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(){
        /**
         * key可以自定义 以前版本中key如果不自定义就返回一个uuid ,导致每次重启服务后所有自动登陆的cookie失效
         * 分布式部署时，如果实例key不同则用户访问系统的另一个实例时自动登陆会失效
         * 此处的key和RememberMeAuthenticationFilter 的key必须一致 当通过记住我功能免密登陆时最后会比较key值
         */
        PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices =
                new PersistentTokenBasedRememberMeServices("security",myUserDetailService,persistentTokenRepository);
        persistentTokenBasedRememberMeServices.setTokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds());
        persistentTokenBasedRememberMeServices.setParameter(securityProperties.getBrowser().getLoginRememberMeParamName());
       //根据前台是否勾选七天免密登录判断是否开启记住我功能 true则直接开启记住我功能 登陆成功在cookie中生成token
        persistentTokenBasedRememberMeServices.setAlwaysRemember(false);
       return  persistentTokenBasedRememberMeServices;
    }

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 重写定义ConcurrentSessionFilter 中session失效策略
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
     * 是通过监听 session 的销毁事件，来及时的清理 session 的记录。用户从不同的浏览器登录后，
     * 都会有对应的 session，当用户注销登录之后，session 就会失效，但是默认的失效是通过调用
     * StandardSession#invalidate 方法来实现的，这一个失效事件无法被 Spring 容器感知到，
     * 进而导致当用户注销登录之后，Spring Security 没有及时清理会话信息表
     *
     * 提供一个 HttpSessionEventPublisher ，这个类实现了 HttpSessionListener 接口，
     * 在该 Bean 中，可以将 session 创建以及销毁的事件及时感知到，
     * 并且调用 Spring 中的事件机制将相关的创建和销毁事件发布出去，进而被 Spring Security 感知到
     * @return
     */
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("==============自定义WebSecurityConfigurerAdapter========");
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
                 * .rememberMe() 表示启用了RememberMeAuthenticationFilter
                 *  .rememberMeServices 则是指定了 RememberMeAuthenticationFilter 中使用persistentTokenBasedRememberMeServices
                 *  通过cookie中rememberme 值查询数据库获取用户信息
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
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/static/**");
    }


}
