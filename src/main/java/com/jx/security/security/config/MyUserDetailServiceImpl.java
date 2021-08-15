package com.jx.security.security.config;

import com.jx.security.entity.Role;
import com.jx.security.entity.User;
import com.jx.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: zhangjx
 * @Date: 2020/4/5 23:04
 * @Description:
 */
@Component("myUserDetailService")
public class MyUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userService.getUserByName(userName);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在!");
        }
        List<Role> roles = userService.qryUserRoles(user.getId());
        user.setRoles(roles);
        return user;
    }
}
