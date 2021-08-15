package com.jx.security.service.impl;

import com.jx.security.dao.MenuMapper;
import com.jx.security.dao.RoleMapper;
import com.jx.security.dao.UserMapper;
import com.jx.security.entity.Menu;
import com.jx.security.entity.Role;
import com.jx.security.entity.User;
import com.jx.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangjx
 * @Date: 2020/6/18 21:47
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    MenuMapper menuMapper;




    @Override
    public User getUserByName(String userName) {
        User user = userMapper.selectOne(User.builder().username(userName).build());
        return user;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        User user = userMapper.selectOne(User.builder().tel(phoneNumber).build());
        return user;
    }

    @Override
    public List<Role> qryUserRoles(Integer userId) {
        return roleMapper.getRolesByUserId(userId);
    }

    @Override
    public List<Menu> qryMenuListByRole(List<Integer> roleIdList) {
        return menuMapper.getMenuListByRole(roleIdList);
    }

    @Override
    public List<Menu> qryMenuTreeRootByRole(List<Integer> roleIdList) {
        return menuMapper.getMenuTreeRootByRole(roleIdList);
    }
}
