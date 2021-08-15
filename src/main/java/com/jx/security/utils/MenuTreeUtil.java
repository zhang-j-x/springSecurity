package com.jx.security.utils;

import com.jx.security.dao.MenuMapper;
import com.jx.security.dto.MenuTreeNodeDto;
import com.jx.security.entity.Menu;
import com.jx.security.vo.user.MenuVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zhangjx
 * @Date: 2020/6/28 22:07
 * @Description: 将菜单封装成树形结构
 */
@Component
public class MenuTreeUtil {

    @Autowired
    MenuMapper menuMapper;

    Map<Integer,List<Integer>> nodeChildRelMap;

    Map<Integer,MenuVo> treeNodeMap;


    /**
     * 构造菜单树
     * @param menus
     * @return
     */
    public List<MenuVo> buildMenuTree(List<Menu> menus){
        List<MenuVo> menuVoList = new ArrayList<>();

        nodeChildRelMap = buildTreeNodeChildRelMap();
        treeNodeMap = buildTreeNodeMap(menus);
        //nodeChildRelMap中key为0的对应list即为根节点
        List<MenuVo> rootMenuTreeNode = nodeChildRelMap.get(0).stream().map(e -> treeNodeMap.get(e)).collect(Collectors.toList());
        //排序
        rootMenuTreeNode.sort(Comparator.comparingInt(MenuVo::getSeq));
        for (MenuVo menu: rootMenuTreeNode) {
            MenuVo menuVo =  buildNode(menu);
            menuVoList.add(menuVo);
        }
        return menuVoList;

    }

    /**
     * 查询根节点数据
     * @param menuVo
     * @return
     */
    private MenuVo buildNode(MenuVo menuVo) {
        List<MenuVo> childList = getChildNodeList(menuVo);
        menuVo.setChildMenu(childList);
        return menuVo;
    }

    /**
     * 递归查询所有子节点
     * @param menuVo
     * @return
     */
    private List<MenuVo> getChildNodeList(MenuVo menuVo){
        List<Integer> childIdList = nodeChildRelMap.get(menuVo.getId());
        if(childIdList == null || childIdList.isEmpty()){
            return null;
        }else {
            List<MenuVo> list = new ArrayList<>();
           for (Integer id : childIdList){
               MenuVo childMenuVo = treeNodeMap.get(id);
               childMenuVo.setChildMenu(getChildNodeList(childMenuVo));
               list.add(childMenuVo);
           }
           //按顺序排序
           list.sort(Comparator.comparingInt(MenuVo::getSeq));
           return list;
        }
    }


    /**
     * 将用户据有权限的所有菜单以id为key节点数据为value 存入map
     * @param menus
     * @return
     */
    Map<Integer,MenuVo> buildTreeNodeMap(List<Menu> menus){
//        Map<Integer,MenuVo> treeNodeMap = new HashMap<>();
//        menus.forEach( e ->{
//                    MenuVo menuVo = new MenuVo();
//                    BeanUtils.copyProperties(e, menuVo);
//                    treeNodeMap.put(e.getId(), menuVo);
//                }
//        );
        Map<Integer,MenuVo> treeNodeMap = menus.stream().map(e ->{
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(e, menuVo);
            return menuVo;
        }).collect(Collectors.toMap(MenuVo::getId,e -> e,(a,b) -> a));
        return treeNodeMap;
    }


    /**
     * 构造存放节点和该节点子节点对应关系的map
     * @return
     */
    Map<Integer,List<Integer>> buildTreeNodeChildRelMap(){
        List<MenuTreeNodeDto> menuTreeNodeDtos = menuMapper.qryTreeNode();
        Map<Integer,List<Integer>> nodeChildMap = new HashMap<>();
        for (MenuTreeNodeDto menuTreeNodeDto : menuTreeNodeDtos) {
            List<Integer> childIdList = getChildIdList(menuTreeNodeDto.getId(),menuTreeNodeDto.getChildIdStr());
            if(childIdList != null && !childIdList.isEmpty()){
                nodeChildMap.put(menuTreeNodeDto.getId(),childIdList);
            }
        }
        return nodeChildMap;
    }

    /**
     * 将数据库查出逗号分隔的子节点字符串转换成list
     * @param nodeId
     * @param childIdStr
     * @return
     */
    public List<Integer> getChildIdList(Integer nodeId,String childIdStr){
        List list = new ArrayList<>();
        if(StringUtils.isNoneEmpty(childIdStr)){
            String[] array = childIdStr.split(",");
            list =  Arrays.stream(array).map(e -> {
                Integer id = Integer.valueOf(e);
                return id;
            }).collect(Collectors.toList());
            list.remove(nodeId);
        }
        return list;
    }


}
