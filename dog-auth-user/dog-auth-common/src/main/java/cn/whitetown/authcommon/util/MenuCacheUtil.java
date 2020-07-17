package cn.whitetown.authcommon.util;

import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.dto.MenuTree;

import java.util.List;

/**
 * 菜单信息缓存操作
 * @author GrainRain
 * @date 2020/07/07 22:05
 **/
public interface MenuCacheUtil {

    /**
     * 初始化菜单操作
     */
    void init();

    /**
     * 销毁时操作
     */
    void destroy();

    /**
     * 菜单数据重置
     */
    void reset();

    /**
     * 保存菜单信息到内存中
     * key为id
     * @param id
     * @param menuTree
     * @return
     */
    MenuTree saveCacheMenu(Long id,MenuTree menuTree);

    /**
     * 通过id获取内存中存储的对应菜单信息
     * @param id
     * @return
     */
    MenuTree getCacheMenu(Long id);

    /**
     * 移除roleId对应的MenuTree
     * @param id
     * @return
     */
    MenuTree removeMenu(Long id);

    /**
     * 更新内存中id对应的菜单信息
     * @param id
     * @param menuTree
     * @return
     */
    MenuTree updateCacheMenu(Long id,MenuTree menuTree);

    /**
     * 保留角色对应的MenuList
     * 包括活跃的和停用的
     * @param roleId
     * @param menuInfos
     * @return
     */
    List<MenuInfo> saveMenuList(Long roleId,List<MenuInfo> menuInfos);

    /**
     * 获取角色对应的MenuList
     * 包括活跃的和停用的
     * @param roleId
     * @return
     */
    List<MenuInfo> getMenuList(Long ... roleId);

    /**
     * 更新角色对应的MenuList
     * @param roleId
     * @param menuInfos
     * @return
     */
    List<MenuInfo> updateMenuList(Long roleId,List<MenuInfo> menuInfos);

    /**
     * 移除roleId对应的MenuList
     * @param roleId
     * @return
     */
    List<MenuInfo> removeMenuList(Long roleId);
}
