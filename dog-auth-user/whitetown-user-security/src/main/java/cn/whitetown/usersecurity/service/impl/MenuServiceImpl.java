package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.ao.MenuInfoAo;
import cn.whitetown.authcommon.entity.ao.RoleMenuConfigure;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.authcommon.util.MenuUtil;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.dto.MenuTree;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.MenuInfoManager;
import cn.whitetown.usersecurity.manager.RoleManager;
import cn.whitetown.usersecurity.mappers.MenuInfoMapper;
import cn.whitetown.usersecurity.service.MenuService;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 菜单管理服务
 * @author GrainRain
 * @date 2020/06/24 22:22
 **/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuInfoMapper,MenuInfo> implements MenuService {
    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Autowired
    private MenuInfoManager menuInfoManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private MenuCacheUtil menuCacheUtil;

    @Autowired
    private AuthUserCacheUtil authUserCacheUtil;

    @Autowired
    private MenuUtil menuUtil;

    @Autowired
    private BeanTransFactory transFactory;

    @Autowired
    private QueryConditionFactory conditionFactory;

    @Override
    public MenuTree queryMenuTree(Long menuId, Integer lowLevel) {
        List<MenuInfo> menuInfos = menuInfoMapper.selectMenuListByIdAndLevel(menuId,lowLevel);
        if (menuInfos.size()==0){
            throw new WhResException(ResponseStatusEnum.NO_THIS_MENU);
        }
        MenuTree menuTree = menuUtil.createMenuTreeByMenuList(menuInfos);
        return menuTree;
    }

    @Override
    public MenuTree queryActiveMenuByUserId(Long userId, Long menuId, Integer lowLevel) {
        List<MenuInfo> menuInfos = menuCacheUtil.getCacheMenuList(userId,menuId,lowLevel);
        if(menuInfos.size() == 0) {
            menuInfos = menuInfoManager.queryActiveMenuByUserId(userId,
                    AuthConstant.ROOT_MENU_ID, AuthConstant.LOWEST_MENU_LEVEL);
            menuCacheUtil.saveCacheMenuList(userId,menuInfos);
            menuInfos = menuCacheUtil.getCacheMenuList(userId,menuId,lowLevel);
        }
        if(menuInfos.size() == 0){
            return new MenuTree();
        }
        return menuUtil.createMenuTreeByMenuList(menuInfos);
    }

    @Override
    public MenuTree queryMenuTreeByRoleName(String roleName) {
        UserRole userRole = roleManager.queryRoleByRoleName(roleName);
        if(userRole == null){
            throw new WhResException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        List<MenuInfo> menuInfos = menuInfoMapper.selectMenuByRoleId(userRole.getRoleId());
        return menuUtil.createMenuTreeByMenuList(menuInfos);
    }

    @Override
    public List<Long> queryMenuIdsByRoleId(Long roleId) {
        UserRole userRole = roleManager.queryRoleById(roleId);
        if(userRole == null){
            throw new WhResException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        List<MenuInfo> menuInfos = menuInfoMapper.selectMenuByRoleId(roleId);
        List<Long> ids = new ArrayList<>();
        menuInfos.forEach(menu -> ids.add(menu.getMenuId()));
        return ids;
    }

    @Override
    public void addSingleMenu(Long createUserId,MenuInfoAo menuInfo) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = conditionFactory.getQueryCondition(MenuInfo.class);
        int[] menuStatusArr = {DogBaseConstant.ACTIVE_NORMAL, DogBaseConstant.DISABLE_WARN};
        queryWrapper.eq(MenuInfo::getMenuCode,menuInfo.getMenuCode())
                .or().eq(MenuInfo::getMenuId,menuInfo.getParentId())
                .in(MenuInfo::getMenuStatus,menuStatusArr);
        if(!DataCheckUtil.checkTextNullBool(menuInfo.getMenuUrl())){
            queryWrapper.or().eq(MenuInfo::getMenuUrl,menuInfo.getMenuUrl());
        }
        List<MenuInfo> oldMenus = menuInfoMapper.selectList(queryWrapper);
        if(oldMenus.size() == 0){
            throw new WhResException(ResponseStatusEnum.MENU_LEVEL_ERROR);
        }else if (oldMenus.size() == 1){
            if(!oldMenus.get(0).getMenuId().equals(menuInfo.getParentId())){
                throw new WhResException(ResponseStatusEnum.EXISTED_THE_MENU);
            }
        }else {
            throw new WhResException(ResponseStatusEnum.REQUEST_INVALIDATE);
        }

        MenuInfo addMenu = transFactory.trans(menuInfo, MenuInfo.class);
        MenuInfo parentMenu = oldMenus.get(0);
        addMenu.setMenuLevel(parentMenu.getMenuLevel()+1);
        addMenu.setCreateUserId(createUserId);
        addMenu.setCreateTime(new Date());
        addMenu.setMenuStatus(DogBaseConstant.ACTIVE_NORMAL);
        menuInfoMapper.insert(addMenu);
    }

    @Override
    public void updateMenuInfo(Long updateUserId,MenuInfoAo menuInfo) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = conditionFactory.getQueryCondition(MenuInfo.class);
        queryWrapper.in(MenuInfo::getMenuId,menuInfo.getMenuId(),menuInfo.getParentId())
                .or().eq(MenuInfo::getMenuCode,menuInfo.getMenuCode());
        if(!DataCheckUtil.checkTextNullBool(menuInfo.getMenuUrl())){
            queryWrapper.or().eq(MenuInfo::getMenuUrl,menuInfo.getMenuUrl());
        }
        List<MenuInfo> menuInfos = menuInfoMapper.selectList(queryWrapper);
        MenuInfo oldMenu = null;
        MenuInfo parentMenu = null;
        int realResultSize = 2;
        if(menuInfos.size() == realResultSize){
            for (MenuInfo menu : menuInfos){
                if(menu.getMenuId().equals(menuInfo.getMenuId())){
                    oldMenu = menu;
                }
                if(menu.getMenuId().equals(menuInfo.getParentId())){
                    parentMenu = menu;
                }
            }
        }else {
            throw new WhResException(ResponseStatusEnum.REQUEST_INVALIDATE);
        }
        if(oldMenu==null || parentMenu == null){
            throw new WhResException(ResponseStatusEnum.REQUEST_INVALIDATE);
        }
        MenuInfo menu = transFactory.trans(menuInfo, MenuInfo.class);
        menu.setUpdateUserId(updateUserId);
        menu.setUpdateTime(new Date());
        menu.setCreateUserId(oldMenu.getUpdateUserId());
        menu.setCreateTime(oldMenu.getCreateTime());
        menu.setMenuLevel(parentMenu.getMenuLevel()+1);
        menu.setMenuStatus(oldMenu.getMenuStatus());
        menuInfoMapper.updateById(menu);
        this.cacheReset();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateMenuStatus(Long menuId, Integer menuStatus) {
        this.checkRootMenuId(menuId);
        MenuInfo menuInfo = menuInfoMapper.selectById(menuId);
        if(menuInfo == null){
            throw new WhResException(ResponseStatusEnum.NO_THIS_MENU);
        }
        //菜单删除，清除关联的所有数据
        if(menuStatus == DogBaseConstant.DELETE_ERROR){
            List<MenuInfo> menuInfos = menuInfoMapper.selectMenuListByIdAndLevel(menuId, AuthConstant.LOWEST_MENU_LEVEL);
            List<Long> menuIds = menuUtil.getMenuIdsFromList(menuInfos);
            menuInfoMapper.deleteRelationAndSubMenu(menuIds);
        }else {
            LambdaUpdateWrapper<MenuInfo> updateCondition = conditionFactory.getUpdateCondition(MenuInfo.class);
            updateCondition.eq(MenuInfo::getMenuId, menuId)
                    .set(MenuInfo::getMenuStatus, menuStatus);
            this.update(updateCondition);
        }
        this.cacheReset();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateRoleMenus(RoleMenuConfigure configure) {
        UserRole userRole = roleManager.queryRoleById(configure.getRoleId());
        if(userRole == null){
            throw new WhResException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        Set menuSet = new LinkedHashSet();
        menuSet.add(AuthConstant.ROOT_MENU_ID);
        Arrays.stream(configure.getMenuIds()).forEach(id->menuSet.add(id));
        menuInfoMapper.updateRoleMenus(configure.getRoleId(),menuSet);
        this.cacheReset();
    }

    /**
     * 校验是否为根节点，根节点禁止任何操作
     * @param menuId
     */
    private void checkRootMenuId(Long menuId){
        if(AuthConstant.ROOT_MENU_ID == menuId){
            throw new WhResException(ResponseStatusEnum.NO_PERMISSION);
        }
    }

    /**
     * 内存涉及权限信息重置
     */
    private void cacheReset() {
        menuCacheUtil.reset();
        authUserCacheUtil.clearAllUsersAuthors();
    }
}
