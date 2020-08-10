package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.dogbase.wache.WhiteExpireMap;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 内存菜单信息处理，默认实现类
 * @author taixian
 * @date 2020/07/10
 **/
public class DefaultMenuCacheUtil implements MenuCacheUtil {

    private Logger logger = LogConstants.SYS_LOGGER;

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
        menuCacheMetaSet = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void destroy() {
        menuCacheMetaSet.stream().forEach(key->expireMap.remove(key));
        menuCacheMetaSet = null;
        logger.info("the menuCache is destroy");
    }

    @Override
    public void reset() {
        menuCacheMetaSet.stream().forEach(key->expireMap.remove(key));
        menuCacheMetaSet.clear();
    }

    @Override
    public List<MenuInfo> saveCacheMenuList(Long userId, List<MenuInfo> menuInfos) {
        List<MenuInfo> result = (List<MenuInfo>)expireMap.putS(AuthConstant.MENU_CACHE_PREFIX_A + userId, menuInfos, AuthConstant.MENU_SAVE_TIME);
        menuCacheMetaSet.add(AuthConstant.MENU_CACHE_PREFIX_A +userId);
        Map<Long,MenuInfo> menuInfoMap = new HashMap<>(16);
        menuInfos.stream().forEach(menuInfo -> menuInfoMap.put(menuInfo.getMenuId(),menuInfo));
        //save menuId<->menuInfo
        expireMap.putS(AuthConstant.MENU_CACHE_PREFIX_M+userId,menuInfoMap,AuthConstant.MENU_SAVE_TIME);
        menuCacheMetaSet.add(AuthConstant.MENU_CACHE_PREFIX_M+userId);
        return result == null ? new ArrayList<>() : result;
    }

    @Override
    public List<MenuInfo> getCacheMenuList(Long userId) {
        if(userId == null){
            return new ArrayList<>();
        }
        List<MenuInfo> resultList = (List<MenuInfo>) expireMap.get(AuthConstant.MENU_CACHE_PREFIX_A + userId);
        return resultList == null ? new ArrayList<>() : resultList;
    }

    @Override
    public List<MenuInfo> getCacheMenuList(Long userId, Long menuId, Integer lowLevel) {
        List<MenuInfo> menuList = this.getCacheMenuList(userId);
        if(menuList.size() == 0) {
            return new ArrayList<>();
        }
        Map<Long,MenuInfo> menuMap = (Map<Long,MenuInfo>) expireMap.get(AuthConstant.MENU_CACHE_PREFIX_M+userId);
        if(menuMap == null) {
            return new ArrayList<>();
        }
        MenuInfo parentMenu = menuMap.get(menuId);
        if(parentMenu == null) {
            return new ArrayList<>();
        }
        this.saveCacheMenuList(userId,menuList);
        List<MenuInfo> resultList = menuList.stream().filter(menuInfo -> menuInfo.getMenuId().equals(menuId) ||
                (menuInfo.getMenuLevel() <= lowLevel && menuInfo.getMenuLevel() > parentMenu.getMenuLevel()))
                .collect(Collectors.toList());
        return resultList;
    }

    @Override
    public List<MenuInfo> removeCacheMenuList(Long userId) {
        List<MenuInfo> result = (List<MenuInfo>)expireMap.remove(AuthConstant.MENU_CACHE_PREFIX_A + userId);
        menuCacheMetaSet.remove(AuthConstant.MENU_CACHE_PREFIX_A +userId);
        return result == null ? new ArrayList<>() : result;
    }
}
