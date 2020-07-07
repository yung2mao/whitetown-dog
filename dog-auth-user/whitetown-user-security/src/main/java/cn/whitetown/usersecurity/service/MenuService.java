package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.ao.MenuInfoAo;
import cn.whitetown.authcommon.entity.ao.RoleMenuConfigure;
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
     * 根据用户ID查询用户可查看的菜单项
     * @param userId
     * @return
     */
    MenuTree queryActiveMenuByUserId(Long userId);

    /**
     * 根据角色查询相应绑定的
     * @param roleName
     * @return
     */
    MenuTree queryMenuTreeByRoleName(String roleName);

    /**
     * 添加一个菜单
     * @param menuInfo
     */
    void addSingleMenu(MenuInfoAo menuInfo);

    /**
     * 修改菜单信息
     * @param menuInfo
     */
    void updateMenuInfo(MenuInfoAo menuInfo);

    /**
     * 菜单状态更新
     * @param menuId
     * @param menuStatus
     */
    void updateMenuStatus(Long menuId, Integer menuStatus);

    /**
     * 角色与菜单信息绑定
     * @param configure
     */
    void updateRoleMenus(RoleMenuConfigure configure);
}
