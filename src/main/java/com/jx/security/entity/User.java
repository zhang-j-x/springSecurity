package com.jx.security.entity;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zjx
 * @since 2020-04-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    /**
     * 登录账号
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;
    /**
     * salt
     */
    private String salt;
    /**
     * 电话
     */
    private String tel;

    /**
     * 状态：0=有效，1=无效
     */
    private Integer status;
    /**
     * 上将登录时间
     */
    @TableField("last_login_time")
    private Date lastLoginTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("modify_time")
    private Date modifyTime;

    private List<Role> roles;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }



    /**
     * @description: springSecurity user实现方法 是否过期
     * @param
     * @return: boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return 0 == status;
    }


    /**
     * @description: springSecurity user实现方法 是否冻结
     * @param
     * @return: boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return status == 0;
    }

    /**
     * @description: springSecurity user实现方法 密码是否过期
     * @param
     * @return: boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @description: springSecurity user实现方法 是否无效被删除
     * @param
     * @return: boolean
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    /**
     * Spring Security 保存用户对象和 session SessionRegistryImpl
     * 集合的 key 就是用户的主体（principal）,而集合的 value 则是一个 set 集合，
     * 这个 set 集合中保存了这个用户对应的 sessionid。ConcurrentMap 集合的 key 是 principal 对象，
     * 用对象做 key，一定要重写 equals 方法和 hashCode 方法，否则第一次存完数据，下次就找不到了
     * @param rhs
     * @return
     */
    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof User) {
            return username.equals(((User) rhs).username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

}
