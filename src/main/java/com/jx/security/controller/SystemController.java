package com.jx.security.controller;

import com.jx.security.entity.Menu;
import com.jx.security.entity.User;
import com.jx.security.service.IUserService;
import com.jx.security.utils.MenuTreeUtil;
import com.jx.security.vo.Rs;
import com.jx.security.vo.user.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhangjx
 * @Date: 2020/6/28 20:40
 * @Description: 系统管理controller
 */
@RestController
public class SystemController {

    @Autowired
    IUserService userService;
    @Autowired
    MenuTreeUtil menuTreeUtil;

    @GetMapping("menu")
    public Rs menu(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Integer> roleIdList = user.getRoles().stream().map(e -> e.getRoleId()).collect(Collectors.toList());
       //当前用户具有权限的所有菜单
        List<Menu> menus = userService.qryMenuListByRole(roleIdList);
        //利用map递归查询菜单树
        List<MenuVo>  menuVoList =  menuTreeUtil.buildMenuTree(menus);
        return Rs.success(menuVoList);
    }

}
