package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.dto.MenuTree;
import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
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
     * 保存角色ID信息
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
        Object result = expireMap.putS(AuthConstant.MENU_CACHE_PREFIX + roleOrUserId, menuTree, AuthConstant.MENU_SAVE_TIME);
        menuCacheMetaSet.add(roleOrUserId+"");
        return (MenuTree) result;
    }

    @Override
    public MenuTree getCacheMenu(Long roleOrUserId) {
        Object result = expireMap.get(AuthConstant.MENU_CACHE_PREFIX + roleOrUserId);
        return result == null ? null : (MenuTree) result;
    }

    @Override
    public MenuTree removeMenu(Long roleOrUserId) {
        Object result = expireMap.remove(AuthConstant.MENU_CACHE_PREFIX + roleOrUserId);
        menuCacheMetaSet.remove(roleOrUserId);
        return result == null ? null : (MenuTree) result;
    }

    @Override
    public MenuTree updateCacheMenu(Long roleOrUserId, MenuTree menuTree) {
        return this.saveCacheMenu(roleOrUserId,menuTree);
    }

    @Override
    public List<MenuInfo> saveMenuList(Long roleId, List<MenuInfo> menuInfos) {
        Object result = expireMap.putS(AuthConstant.MENU_CACHE_PREFIX + roleId, menuInfos, AuthConstant.MENU_SAVE_TIME);
        menuCacheMetaSet.add(AuthConstant.MENU_CACHE_PREFIX +roleId);
        return (List<MenuInfo>) result;
    }

    @Override
    public List<MenuInfo> getMenuList(Long ... roleIds) {
        if(roleIds == null || roleIds.length==0){
            return new ArrayList<>();
        }
        List<MenuInfo> menuInfos = new ArrayList<>();
        Arrays.stream(roleIds).forEach(roleId->{
            Object menus = expireMap.get(AuthConstant.MENU_CACHE_PREFIX + roleId);
            if(menus == null) { return; }
            ((List<MenuInfo>)menus).forEach(menu->menuInfos.add(menu));
        });
        return menuInfos.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<MenuInfo> updateMenuList(Long roleId, List<MenuInfo> menuInfos) {
        return this.saveMenuList(roleId,menuInfos);
    }

    @Override
    public List<MenuInfo> removeMenuList(Long roleId) {
        Object result = expireMap.remove(AuthConstant.MENU_CACHE_PREFIX + roleId);
        menuCacheMetaSet.remove(AuthConstant.MENU_CACHE_PREFIX +roleId);
        return result == null ? null : (List<MenuInfo>) result;
    }
}
