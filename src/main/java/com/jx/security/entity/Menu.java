package com.jx.security.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangjx
 * @since 2020-06-21
 */
@Data
public class Menu extends Model<Menu> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String component;
    private String path;
    private String name;
    private String code;
    private String icon;
    private String status;
    @TableField("require_auth")
    private Integer requireAuth;
    private Integer pid;

    private Integer seq;
    private Integer level;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
