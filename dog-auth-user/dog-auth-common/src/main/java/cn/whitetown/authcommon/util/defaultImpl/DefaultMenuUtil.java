package cn.whitetown.authcommon.util.defaultImpl;

import cn.whitetown.authcommon.util.MenuUtil;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.vo.MenuTree;
import org.springframework.beans.factory.annotation.Autowired;

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
     * 根据父级tree和list构建树形菜单结构
     * @param parentMenuTree
     * @param menuInfos
     * @return
     */
    @Override
    public MenuTree createMenuTreeByMenuList(MenuTree parentMenuTree, List<MenuInfo> menuInfos) {
        try {
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
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return parentMenuTree;
    }
}
