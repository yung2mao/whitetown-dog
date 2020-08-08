package cn.whitetown.authcommon.util;

import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.dogbase.wache.WhiteCacheBase;

import java.util.List;

/**
 * 菜单信息缓存操作
 * @author GrainRain
 * @date 2020/07/07 22:05
 **/
public interface MenuCacheUtil extends WhiteCacheBase {

    /**
     * 菜单数据重置
     */
    void reset();

    /**
     * 保存key对应的MenuList
     * 包括活跃的和停用的
     * @param key
     * @param menuInfos
     * @return
     */
    List<MenuInfo> saveCacheMenuList(Long key, List<MenuInfo> menuInfos);

    /**
     * 获取保存的MenuList
     * 包括活跃的和停用的
     * @param key
     * @return
     */
    List<MenuInfo> getCacheMenuList(Long key);

    /**
     * 获取活跃状态的menuList
     * 指定parentId和最低层级
     * @param key
     * @param menuId
     * @param lowLevel
     * @return
     */
    List<MenuInfo> getCacheMenuList(Long key, Long menuId, Integer lowLevel);

    /**
     * 移除key对应的MenuList
     * @param
     * @return
     */
    List<MenuInfo> removeCacheMenuList(Long key);
}
