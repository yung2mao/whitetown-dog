package cn.whitetown.usersecurity.manager;

import cn.whitetown.authcommon.entity.dto.MenuTree;
import cn.whitetown.authcommon.entity.po.MenuInfo;

import java.util.List;

/**
 * @author taixian
 * @date 2020/07/30
 **/
public interface MenuInfoManager {

    /**
     * 查询用户可访问菜单项
     * @param userId
     * @param menuId
     * @param lowLevel
     * @return
     */
    List<MenuInfo> queryActiveMenuByUserId(Long userId, Long menuId, Integer lowLevel);
}
