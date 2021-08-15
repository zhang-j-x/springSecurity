package com.jx.security.dao;

import com.jx.security.dto.MenuTreeNodeDto;
import com.jx.security.entity.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色查询可访问菜单
     * @param roleIdList
     * @return
     */
    List<Menu> getMenuListByRole(List<Integer> roleIdList);

    /**
     * 获取菜单每个节点的子节点
     * @return
     */
    List<MenuTreeNodeDto> qryTreeNode();

    /**
     * 获取菜单列表的根节点
     * @param roleIdList
     * @return
     */
    List<Menu> getMenuTreeRootByRole(List<Integer> roleIdList);

}
