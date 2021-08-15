package com.jx.security.service.impl;

import com.jx.security.dao.UserMapper;
import com.jx.security.entity.User;
import com.jx.security.redis.utils.RedisUtil;
import com.jx.security.service.IUserService;
import com.jx.security.utils.JsonFormatUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: zhangjx
 * @Date: 2020/6/18 21:35
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private IUserService userService;
    @Autowired
    RedisUtil redisUtil;

    @Test
    public void userTest(){
//        User user = userService.getUserByName("zhangjx");
//        System.out.println(JsonFormatUtil.toJson(user));
//        redisUtil.set("test", "tets");
//        String test = (String) redisUtil.get("test");
//        System.out.println("==========" + test);
    }
}