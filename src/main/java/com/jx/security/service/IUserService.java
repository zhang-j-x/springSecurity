package com.jx.security.service;

import com.jx.security.entity.Menu;
import com.jx.security.entity.Role;
import com.jx.security.entity.User;

import java.util.List;

/**
 * @Author: zhangjx
 * @Date: 2020/6/18 21:46
 * @Description:
 */
public interface IUserService {
    /**
     * 通过用户名获取用户
     * @param userName
     * @return
     */
    User getUserByName(String userName);

    /**
     * 通过电话号码查询用户
     * @param phoneNumber
     * @return
     */
    User getUserByPhoneNumber(String phoneNumber);

    /**
     * 查询用户角色
     * @param userId
     * @return
     */
    List<Role> qryUserRoles(Integer userId);

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> qryMenuListByRole(List<Integer> roleIdList);

    /**
     * 根据角色获取菜单树根节点
     * @return
     */
    List<Menu> qryMenuTreeRootByRole(List<Integer> roleIdList);
}
