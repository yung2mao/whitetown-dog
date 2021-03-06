package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.ao.MenuInfoAo;
import cn.whitetown.authcommon.entity.ao.RoleMenuConfigure;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.dto.MenuTree;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单管理服务
 * @author GrainRain
 * @date 2020/06/24 22:22
 **/
public interface MenuService extends IService<MenuInfo> {
    /**
     * 获取菜单的树形结构
     * @param menuId
     * @param lowLevel
     * @return
     */
    MenuTree queryMenuTree(Long menuId, Integer lowLevel);

    /**
     * 根据用户ID查询用户可查看的菜单项
     * @param userId
     * @param menuId
     * @param lowLevel
     * @return
     */
    MenuTree queryActiveMenuByUserId(Long userId, Long menuId, Integer lowLevel);

    /**
     * 根据角色查询相应绑定的菜单信息
     * @param roleName
     * @return
     */
    MenuTree queryMenuTreeByRoleName(String roleName);

    /**
     * 根据角色ID查询绑定的菜单ID
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdsByRoleId(Long roleId);

    /**
     * 添加一个菜单
     * @param createUserId 创建人
     * @param menuInfo
     */
    void addSingleMenu(Long createUserId,MenuInfoAo menuInfo);

    /**
     * 修改菜单信息
     * @param updateUserId 修改人
     * @param menuInfo
     */
    void updateMenuInfo(Long updateUserId,MenuInfoAo menuInfo);

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
