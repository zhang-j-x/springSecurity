package com.jx.security.dao;

import com.jx.security.entity.Role;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangjx
 * @since 2020-06-21
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过用户id查询角色信息
     * @param userId
     * @return
     */
    List<Role> getRolesByUserId(@Param("userId") Integer userId);
}
