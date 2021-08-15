package com.jx.security.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * springSecurity  密码加密
 * @Author: zhangjx
 * @Date: 2020/4/5 23:25
 * @Description:
 */
@Configuration
public class PasswordEncoderConfig {
    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("zjx!@#123");
        System.out.println(encode);
    }
}
