package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.dto.MenuTree;
import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 内存菜单信息处理，默认实现类
 * @author taixian
 * @date 2020/07/10
 **/
public class DefaultMenuCacheUtil implements MenuCacheUtil {

    @Autowired
    private WhiteExpireMap expireMap;
    /**
     * 保存存储数据的key信息
     */
    private Set<String> menuCacheMetaSet = null;

    public DefaultMenuCacheUtil() {
        this.init();
    }

    @Override
    public void init() {
        menuCacheMetaSet = new CopyOnWriteArraySet<>();
    }

    @PreDestroy
    @Override
    public void destroy() {
        menuCacheMetaSet.stream().forEach(key->expireMap.remove(key));
        menuCacheMetaSet = null;
        System.out.println("the menuCache is destroy");
    }

    @Override
    public void reset() {
        menuCacheMetaSet.stream().forEach(key->expireMap.remove(key));
        menuCacheMetaSet.clear();
    }

    @Override
    public MenuTree saveCacheMenu(Long roleOrUserId,MenuTree menuTree) {
        Object result = expireMap.putS(AuthConstant.MENU_CACHE_PREFIX_A + roleOrUserId, menuTree, AuthConstant.MENU_SAVE_TIME);
        menuCacheMetaSet.add(roleOrUserId+"");
        return (MenuTree) result;
    }

    @Override
    public MenuTree getCacheMenu(Long roleOrUserId) {
        Object result = expireMap.get(AuthConstant.MENU_CACHE_PREFIX_A + roleOrUserId);
        return result == null ? null : (MenuTree) result;
    }

    @Override
    public MenuTree removeCacheMenu(Long roleOrUserId) {
        Object result = expireMap.remove(AuthConstant.MENU_CACHE_PREFIX_A + roleOrUserId);
        menuCacheMetaSet.remove(roleOrUserId);
        return result == null ? null : (MenuTree) result;
    }

    @Override
    public List<MenuInfo> saveMenuList(Long userId, List<MenuInfo> menuInfos) {
        Object result = expireMap.putS(AuthConstant.MENU_CACHE_PREFIX_B + userId, menuInfos, AuthConstant.MENU_SAVE_TIME);
        menuCacheMetaSet.add(AuthConstant.MENU_CACHE_PREFIX_B +userId);
        Map<Long,MenuInfo> menuInfoMap = new HashMap<>();
        menuInfos.stream().forEach(menuInfo -> menuInfoMap.put(menuInfo.getMenuId(),menuInfo));
        //save menuId<->menuInfo
        expireMap.putS(AuthConstant.MENU_CACHE_PREFIX_M+userId,menuInfoMap,AuthConstant.MENU_SAVE_TIME);
        menuCacheMetaSet.add(AuthConstant.MENU_CACHE_PREFIX_M+userId);
        return (List<MenuInfo>) result;
    }

    @Override
    public List<MenuInfo> getMenuList(Long userId) {
        if(userId == null){
            return new ArrayList<>();
        }
        return (List<MenuInfo>) expireMap.get(AuthConstant.MENU_CACHE_PREFIX_B + userId);
    }

    @Override
    public List<MenuInfo> getMenuList(Long userId, Long menuId, Integer lowLevel) {
        MenuInfo parentMenu = ((Map<Long,MenuInfo>) expireMap.get(AuthConstant.MENU_CACHE_PREFIX_M+userId)).get(menuId);
        if(parentMenu == null) {
            return new ArrayList<>();
        }
        List<MenuInfo> menuList = this.getMenuList(userId);
        if(menuList == null) {
            return new ArrayList<>();
        }
        this.saveMenuList(userId,menuList);
        List<MenuInfo> resultList = menuList.stream().filter(menuInfo -> {
            return menuInfo.getMenuId().equals(menuId) ||
                    (menuInfo.getMenuLevel() <= lowLevel && menuInfo.getMenuLevel() > parentMenu.getMenuLevel());
        }).collect(Collectors.toList());
        System.out.println(resultList.size()+","+menuList.size());
        resultList.add(parentMenu);
        return resultList;
    }

    @Override
    public List<MenuInfo> removeMenuList(Long userId) {
        Object result = expireMap.remove(AuthConstant.MENU_CACHE_PREFIX_B + userId);
        menuCacheMetaSet.remove(AuthConstant.MENU_CACHE_PREFIX_A +userId);
        return result == null ? null : (List<MenuInfo>) result;
    }
}
