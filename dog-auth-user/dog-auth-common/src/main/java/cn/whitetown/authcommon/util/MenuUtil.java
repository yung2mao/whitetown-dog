package cn.whitetown.authcommon.util;

import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.vo.MenuTree;

import java.util.List;

/**
 * 菜单操作工具
 * @author GrainRain
 * @date 2020/06/25 15:25
 **/
public interface MenuUtil {
    /**
     * 将MenuInfo list结构构建为树形结构
     * @param menuInfos
     * @return
     */
    MenuTree createMenuTreeByMenuList(List<MenuInfo> menuInfos);

    /**
     * 根据menuInfos获取所需树形结构下菜单的ID集合
     * @param menuInfos
     * @return
     */
    List<Long> getMenuIdsFromList(List<MenuInfo> menuInfos);
}
