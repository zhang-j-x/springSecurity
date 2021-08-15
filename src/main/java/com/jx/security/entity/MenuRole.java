package com.jx.security.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangjx
 * @since 2020-06-21
 */
@TableName("menu_role")
public class MenuRole extends Model<MenuRole> {

    private static final long serialVersionUID = 1L;

    private Long id;
    @TableField("menu_id")
    private Integer menuId;
    @TableField("role_id")
    private Integer roleId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MenuRole{" +
        ", id=" + id +
        ", menuId=" + menuId +
        ", roleId=" + roleId +
        "}";
    }
}
