package com.jx.security.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jx.security.entity.Role;
import com.jx.security.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zjx
 * @since 2020-04-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {



}
