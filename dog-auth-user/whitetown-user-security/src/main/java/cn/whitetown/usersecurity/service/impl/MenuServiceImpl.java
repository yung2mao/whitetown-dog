package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.ao.MenuInfoAo;
import cn.whitetown.authcommon.entity.ao.RoleMenuConfigure;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.authcommon.util.MenuUtil;
import cn.whitetown.dogbase.common.constant.DogConstant;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.vo.MenuTree;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.RoleManager;
import cn.whitetown.usersecurity.mappers.MenuInfoMapper;
import cn.whitetown.usersecurity.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    private RoleManager roleManager;

    @Autowired
    private MenuCacheUtil menuCacheUtil;

    @Autowired
    private MenuUtil menuUtil;

    @Autowired
    private BeanTransFactory transFactory;

    @Autowired
    private QueryConditionFactory conditionFactory;

    /**
     * 获取菜单的树形结构
     * @param menuId
     * @param lowLevel
     * @return
     */
    @Override
    public MenuTree queryMenuTree(Long menuId, Integer lowLevel) {
        List<MenuInfo> menuInfos = menuInfoMapper.selectMenuListByIdAndLevel(menuId,lowLevel);
        if (menuInfos.size()==0){
            throw new CustomException(ResponseStatusEnum.NO_THIS_MENU);
        }
        menuInfos = menuInfos.stream().sorted(Comparator.comparing(MenuInfo::getMenuSort)).collect(Collectors.toList());
        MenuTree menuTree = menuUtil.createMenuTreeByMenuList(menuInfos);
        return menuTree;
    }

    /**
     * 根据用户ID查询可查阅的的菜单项
     * @param userId
     * @return
     */
    @Override
    public MenuTree queryActiveMenuByUserId(Long userId) {
        List<MenuInfo> menuInfos = menuInfoMapper.selectActiveMenuByUserId(DogConstant.ACTIVE_NORMAL,userId);
        if(menuInfos.size() == 0){
            return new MenuTree();
        }
        menuInfos = menuInfos.stream().sorted(Comparator.comparing(MenuInfo::getMenuSort)).collect(Collectors.toList());
        return menuUtil.createMenuTreeByMenuList(menuInfos);
    }

    /**
     * 根据角色名称查询绑定的菜单信息
     * @param roleName
     * @return
     */
    @Override
    public MenuTree queryMenuTreeByRoleName(String roleName) {
        UserRole userRole = roleManager.queryRoleByRoleName(roleName);
        if(userRole == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        MenuTree menuTree = menuCacheUtil.getCacheMenu(userRole.getRoleId());
        if(menuTree == null){
            List<MenuInfo> menuInfos = menuInfoMapper.selectMenuByRoleId(userRole.getRoleId());
            menuTree = menuUtil.createMenuTreeByMenuList(menuInfos);
        }
        menuCacheUtil.saveCacheMenu(userRole.getRoleId(),menuTree);
        return menuTree;
    }

    /**
     * 添加菜单信息
     * @param menuInfo
     */
    @Override
    public void addSingleMenu(Long createUserId,MenuInfoAo menuInfo) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = conditionFactory.getQueryCondition(MenuInfo.class);
        int[] menuStatusArr = {DogConstant.ACTIVE_NORMAL,DogConstant.DISABLE_WARN};
        queryWrapper.eq(MenuInfo::getMenuCode,menuInfo.getMenuCode())
                .or().eq(MenuInfo::getMenuUrl,menuInfo.getMenuUrl())
                .or().eq(MenuInfo::getMenuId,menuInfo.getParentId())
                .in(MenuInfo::getMenuStatus,menuStatusArr);
        List<MenuInfo> oldMenus = menuInfoMapper.selectList(queryWrapper);
        if(oldMenus.size() == 0){
            throw new CustomException(ResponseStatusEnum.MENU_LEVEL_ERROR);
        }else if (oldMenus.size() == 1){
            if(oldMenus.get(0).getMenuId().equals(menuInfo.getParentId())){
                throw new CustomException(ResponseStatusEnum.EXISTED_THE_MENU);
            }
        }else {
            throw new CustomException(ResponseStatusEnum.REQUEST_INVALIDATE);
        }

        MenuInfo addMenu = transFactory.trans(menuInfo, MenuInfo.class);
        MenuInfo parentMenu = oldMenus.get(0);
        addMenu.setMenuLevel(parentMenu.getMenuLevel()+1);
        addMenu.setCreateUserId(createUserId);
        addMenu.setCreateTime(new Date());
        addMenu.setMenuStatus(DogConstant.ACTIVE_NORMAL);
        menuInfoMapper.insert(addMenu);
    }

    /**
     * 修改菜单信息
     * @param menuInfo
     */
    @Override
    public void updateMenuInfo(Long updateUserId,MenuInfoAo menuInfo) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = conditionFactory.getQueryCondition(MenuInfo.class);
        queryWrapper.eq(MenuInfo::getMenuId,menuInfo.getMenuId())
                .or().eq(MenuInfo::getMenuCode,menuInfo.getMenuCode())
                .or().eq(MenuInfo::getMenuUrl,menuInfo.getMenuUrl())
                .or().eq(MenuInfo::getMenuId,menuInfo.getParentId());
        List<MenuInfo> menuInfos = menuInfoMapper.selectList(queryWrapper);
        MenuInfo oldMenu = null;
        MenuInfo parentMenu = null;
        if(menuInfos.size() == 2){
            for (MenuInfo menu : menuInfos){
                if(menu.getMenuId().equals(menuInfo.getMenuId())){
                    oldMenu = menu;
                }
                if(menu.getMenuId().equals(menuInfo.getParentId())){
                    parentMenu = menu;
                }
            }
        }else {
            throw new CustomException(ResponseStatusEnum.REQUEST_INVALIDATE);
        }
        if(oldMenu==null || parentMenu == null){
            throw new CustomException(ResponseStatusEnum.REQUEST_INVALIDATE);
        }
        MenuInfo menu = transFactory.trans(menuInfo, MenuInfo.class);
        menu.setUpdateUserId(updateUserId);
        menu.setUpdateTime(new Date());
        menu.setCreateUserId(oldMenu.getUpdateUserId());
        menu.setCreateTime(oldMenu.getCreateTime());
        menu.setMenuLevel(parentMenu.getMenuLevel()+1);
        menu.setMenuStatus(oldMenu.getMenuStatus());
        menuInfoMapper.updateById(menu);
        menuCacheUtil.reset();
    }

    /**
     * 菜单状态变更
     * @param menuId
     * @param menuStatus
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateMenuStatus(Long menuId, Integer menuStatus) {
        this.checkRootMenuId(menuId);
        MenuInfo menuInfo = menuInfoMapper.selectById(menuId);
        if(menuInfo == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_MENU);
        }
        //菜单删除，清除关联的所有数据
        if(menuStatus == DogConstant.DELETE_ERROR){
            List<MenuInfo> menuInfos = menuInfoMapper.selectMenuListByIdAndLevel(menuId, AuthConstant.LOWEST_MENU_LEVEL);
            List<Long> menuIds = menuUtil.getMenuIdsFromList(menuInfos);
            menuInfoMapper.deleteRelationAndSubMenu(menuIds);
        }else {
            LambdaUpdateWrapper<MenuInfo> updateCondition = conditionFactory.getUpdateCondition(MenuInfo.class);
            updateCondition.eq(MenuInfo::getMenuId, menuId)
                    .set(MenuInfo::getMenuStatus, menuStatus);
            this.update(updateCondition);
        }
        menuCacheUtil.reset();
    }

    /**
     * 更新角色与菜单的绑定信息
     * @param configure
     */
    @Override
    public void updateRoleMenus(RoleMenuConfigure configure) {
        UserRole userRole = roleManager.queryRoleById(configure.getRoleId());
        if(userRole == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        Set menuSet = new LinkedHashSet();
        menuSet.add(1);
        Arrays.stream(configure.getMenuIds()).forEach(id->menuSet.add(id));
        menuInfoMapper.updateRoleMenus(configure.getRoleId(),menuSet);
        menuCacheUtil.reset();
    }

    /**
     * 校验是否为根节点，根节点禁止任何操作
     * @param menuId
     */
    private void checkRootMenuId(Long menuId){
        if(menuId == 1){
            throw new CustomException(ResponseStatusEnum.NO_PERMISSION);
        }
    }
}
