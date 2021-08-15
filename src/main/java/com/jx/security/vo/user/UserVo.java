package com.jx.security.vo.user;

import com.jx.security.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * @Author: zhangjx
 * @Date: 2020/7/10 17:34
 * @Description:
 */
@Data
public class UserVo {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 登录账号
     */
    private String username;

    private List<Role> roles;


}
