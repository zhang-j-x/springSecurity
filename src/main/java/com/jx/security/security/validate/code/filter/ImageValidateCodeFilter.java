package com.jx.security.security.validate.code.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jx.security.properties.SecurityProperties;
import com.jx.security.security.config.MyAuthenticationFailureHandler;
import com.jx.security.security.validate.code.constant.ValidateCodeConstant;
import com.jx.security.security.validate.code.dto.ResultDto;
import com.jx.security.security.validate.code.exception.ValidateCodeException;
import com.jx.security.security.validate.code.generator.ImageCodeGenerator;
import com.jx.security.utils.BufferedServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zhangjx
 * @Date: 2020/4/18 22:13
 * @Description:
 */
@Component
@Slf4j
public class ImageValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private ImageCodeGenerator imageCodeGenerator;
    @Autowired
    private SecurityProperties securityProperties;
    /**路径判断工具类*/
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    /**需要图形验证码拦截的url*/
    private Set<String> urls = new HashSet<>();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls = StringUtils.split(securityProperties.getValidateCode().getImageCode().getUrls(), ",");
        for (String configUrl: configUrls) {
            urls.add(configUrl);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 解决request.getinputstream() 流重复读取问题
        request = new BufferedServletRequestWrapper(request);
        //判断是否是登录请求
        String requestURI = request.getRequestURI();
        Boolean needValidate = false;
        for (String url: urls) {
            if(pathMatcher.match(url, requestURI)){
                needValidate = true;
                break;
            }
        }
        if (needValidate){
            try{
                validateImageCode(request);
            }catch (ValidateCodeException e){
                myAuthenticationFailureHandler.onAuthenticationFailure(request,response ,e );
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validateImageCode(HttpServletRequest request) {
        String uuid = null;
        String imageCode = null;
        if(RequestMethod.POST.toString().equals(request.getMethod())
                &&request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)){
            try {
                Map<String,String> map = new ObjectMapper().readValue(request.getInputStream(),Map.class );
                uuid = map.get(securityProperties.getValidateCode().getImageCode().getUuidParamName());
                imageCode = map.get(securityProperties.getValidateCode().getImageCode().getValidateCodeParamName());
            } catch (Exception e) {
                log.error("图片验证码验证 获取post参数异常！", e);
                throw new RuntimeException("图片验证码验证 获取post参数异常！" + e.getMessage());
            }
        }else if(RequestMethod.GET.toString().equals(request.getMethod())){
             uuid = request.getParameter(securityProperties.getValidateCode().getImageCode().getUuidParamName());
             imageCode = request.getParameter(securityProperties.getValidateCode().getImageCode().getValidateCodeParamName());
        }else {
            log.error("图片验证码验证 获取参数异常！");
            throw new RuntimeException("图片验证码验证 获取参数异常！");
        }

        if(StringUtils.isBlank(imageCode)){
            throw  new ValidateCodeException(ValidateCodeConstant.VALIDATE_CODE_MUST_NOT_NULL);
        }
        ResultDto resultDto = imageCodeGenerator.judgeValidateCodeRight(uuid, imageCode);
        if(!resultDto.getSuccess()){
            throw  new ValidateCodeException(resultDto.getMsg());
        }
    }

}
