package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.vo.MenuTree;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 菜单管理服务
 * @author GrainRain
 * @date 2020/06/24 22:22
 **/
public interface MenuService extends IService<MenuInfo> {
    /**
     * 获取菜单的树形结构
     * @param menuCode
     * @param lowLevel
     * @return
     */
    MenuTree queryMenuTree(String menuCode, Integer lowLevel);

    /**
     * 添加一个菜单
     * @param menuInfo
     */
    void addSingleMenu(MenuInfo menuInfo);

    /**
     * 菜单状态更新
     * @param menuId
     * @param menuStatus
     */
    void updateMenuStatus(Long menuId, Integer menuStatus);
}
