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
     * 根据父级tree和list构建树形菜单结构
     * @param parentMenuTree
     * @param menuInfos
     * @return
     */
    MenuTree createMenuTreeByMenuList(MenuTree parentMenuTree,List<MenuInfo> menuInfos);
}
