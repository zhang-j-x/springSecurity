package com.jx.security.controller;

import com.alibaba.fastjson.JSON;
import com.jx.security.form.RegisterForm;
import com.jx.security.security.authentication.details.MyWebAuthenticationDetails;
import com.jx.security.service.IUserService;
import com.jx.security.utils.RsaEncryptUtil;
import com.jx.security.vo.Rs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangjx
 * @Date: 2020/4/11 19:35
 * @Description:
 */
@RequestMapping("/user/")
@RestController
public class UserController {
    @Autowired
    SessionRegistry  sessionRegistry;
    @Autowired
    IUserService userService;
    @Autowired
    RsaEncryptUtil rsaEncryptUtil;


    @PostMapping("register")
    public Rs register(@RequestBody RegisterForm registerForm){
        System.out.println("============register====================");
        System.out.println(JSON.toJSONString(registerForm));
        return Rs.success();
    }


    /***
     * 登录后如何拿到用户信息
     *
     */
    @GetMapping("getLoginUserInfo")
    public MyWebAuthenticationDetails getLoginUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();
        return details;
    }

    /**
     * 获取RSA加密公钥
     * @return
     */
    @GetMapping("getKey")
    public Rs getKey(){
        return Rs.success(rsaEncryptUtil.getPublicKey());
    }


    @GetMapping("logout")
    public Rs logout(){
        return Rs.success();
    }




}
