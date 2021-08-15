package com.jx.security.vo.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhangjx
 * @Date: 2020/6/26 20:04
 * @Description:
 */
@Data
public class MenuVo {

    private Integer id;
    private String component;
    private String path;
    private String name;
    private String code;
    private String icon;
    private String status;
    private Integer requireAuth;
    private Integer pid;
    private Integer seq;
    private Integer level;

    List<MenuVo> childMenu = new ArrayList<>();
}
