package com.jx.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
@MapperScan("com.jx.security.dao")
public class SpringSecurityApplication {


    public static void main(String[] args) {


        SpringApplication.run(SpringSecurityApplication.class, args);

    }



}
