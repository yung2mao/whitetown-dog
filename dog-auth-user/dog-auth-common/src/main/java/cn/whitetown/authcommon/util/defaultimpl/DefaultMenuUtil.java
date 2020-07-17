package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.util.MenuUtil;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.dto.MenuTree;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 菜单相关操作的工具类
 * @author GrainRain
 * @date 2020/06/25 11:23
 **/
public class DefaultMenuUtil implements MenuUtil {
    @Autowired
    private BeanTransFactory beanTrans;

    /**
     * 将MenuInfo list结构构建为树形结构
     * @param menuInfos
     * @return
     */
    @Override
    public MenuTree createMenuTreeByMenuList(List<MenuInfo> menuInfos){
        return this.createMenuTreeByMenuList(null,menuInfos);
    }

    /**
     * 获取树形结构中菜单对应的ID集合
     * @param menuInfos
     * @return
     */
    @Override
    public List<Long> getMenuIdsFromList(List<MenuInfo> menuInfos) {
        return this.getMenuIdsFromList(null,null,menuInfos);
    }

    /**
     * 获取菜单树中所有ID信息
     * @param menuTree
     * @return
     */
    @Override
    public List<Long> getMenuIdsFromTree(MenuTree menuTree) {
        return this.getMenuIdsFromTree(null,menuTree);
    }

    /**
     * 根据父级tree和list构建树形菜单结构
     * @param parentMenuTree
     * @param menuInfos
     * @return
     */
    public MenuTree createMenuTreeByMenuList(MenuTree parentMenuTree, List<MenuInfo> menuInfos) {
        if (parentMenuTree == null) {
            MenuInfo menuInfo = menuInfos.stream().min(Comparator.comparing(MenuInfo::getMenuLevel)).get();
            parentMenuTree = beanTrans.trans(menuInfo, MenuTree.class);
            menuInfos.remove(menuInfo);
        }
        for (MenuInfo menu : menuInfos) {
            if (menu.getParentId().equals(parentMenuTree.getMenuId())) {
                MenuTree menuTree = beanTrans.trans(menu, MenuTree.class);
                parentMenuTree.getChildren().add(menuTree);
                this.createMenuTreeByMenuList(menuTree, menuInfos);
            }
        }
        return parentMenuTree;
    }

    public List<Long> getMenuIdsFromList(List<Long> menuIds, MenuInfo parentMenu, List<MenuInfo> menuInfos) {
        if(menuIds == null){
            menuIds = new ArrayList<>();
        }

        if(parentMenu == null){
            parentMenu = menuInfos.stream().min(Comparator.comparing(MenuInfo::getMenuLevel)).get();
            menuIds.add(parentMenu.getMenuId());
            menuInfos.remove(parentMenu);
        }

        for(MenuInfo menu:menuInfos){
            if(menu.getParentId().equals(parentMenu.getMenuId())){
                menuIds.add(menu.getMenuId());
                this.getMenuIdsFromList(menuIds,menu,menuInfos);
            }
        }
        return menuIds;
    }

    public List<Long> getMenuIdsFromTree(List<Long> menuIds,MenuTree menuTree){
        if(menuIds == null){
            menuIds = new ArrayList<>();
        }
        if (menuTree == null){
            return new ArrayList<>();
        }
        menuIds.add(menuTree.getMenuId());
        if(menuTree.getChildren().size() != 0){
            for(MenuTree menu:menuTree.getChildren()){
                this.getMenuIdsFromTree(menuIds,menu);
            }
        }
        return menuIds;
    }
}
