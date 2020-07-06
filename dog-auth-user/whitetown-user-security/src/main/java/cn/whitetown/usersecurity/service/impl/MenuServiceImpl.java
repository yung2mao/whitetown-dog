package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.util.MenuUtil;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.vo.MenuTree;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.mappers.MenuInfoMapper;
import cn.whitetown.usersecurity.mappers.RoleInfoMapper;
import cn.whitetown.usersecurity.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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

    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MenuUtil menuUtil;

    @Autowired
    private QueryConditionFactory conditionFactory;

    /**
     * 获取菜单的树形结构
     * @param menuCode
     * @param lowLevel
     * @return
     */
    @Override
    public MenuTree queryMenuTree(String menuCode, Integer lowLevel) {
        List<MenuInfo> menuInfos = menuInfoMapper.selectMenuListByCodeAndLevel(menuCode,lowLevel);
        if (menuInfos.size()==0){
            return null;
        }
        menuInfos = menuInfos.stream().sorted(Comparator.comparing(MenuInfo::getMenuSort)).collect(Collectors.toList());
        MenuTree menuTree = menuUtil.createMenuTreeByMenuList(menuInfos);
        return menuTree;
    }

    /**
     * 根据用户ID查询配置的菜单项
     * @param userId
     * @return
     */
    @Override
    public MenuTree queryActiveMenuByUserId(Long userId) {
        List<MenuInfo> menuInfos = menuInfoMapper.selectActiveMenuByUserId(0,userId);
        return menuUtil.createMenuTreeByMenuList(menuInfos);
    }

    /**
     * 根据角色名称查询绑定的菜单信息
     * @param roleName
     * @return
     */
    @Override
    public MenuTree queryMenuTreeByRoleName(String roleName) {
        LambdaQueryWrapper<UserRole> queryWrapper = conditionFactory.getQueryCondition(UserRole.class);
        queryWrapper.eq(UserRole::getName,roleName);
        UserRole userRole = roleInfoMapper.selectOne(queryWrapper);
        if(userRole == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        List<MenuInfo> menuInfos = menuInfoMapper.selectMenuByRoleId(userRole.getRoleId());
        return menuUtil.createMenuTreeByMenuList(menuInfos);
    }

    /**
     * 添加菜单信息
     * @param menuInfo
     */
    @Override
    public void addSingleMenu(MenuInfo menuInfo) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = conditionFactory.getQueryCondition(MenuInfo.class);
        queryWrapper.eq(MenuInfo::getMenuCode,menuInfo.getMenuCode());
        MenuInfo oldMenu = menuInfoMapper.selectOne(queryWrapper);
        if(oldMenu != null){
            throw new CustomException(ResponseStatusEnum.EXISTED_THE_MENU);
        }
        //parent menu
        LambdaUpdateWrapper<MenuInfo> parentQueryWrapper = conditionFactory.getUpdateCondition(MenuInfo.class);
        parentQueryWrapper.eq(MenuInfo::getMenuId,menuInfo.getParentId());
        MenuInfo parentMenu = menuInfoMapper.selectOne(parentQueryWrapper);
        if(parentMenu == null){
            throw new CustomException(ResponseStatusEnum.MENU_LEVEL_ERROR);
        }
        menuInfo.setMenuLevel(parentMenu.getMenuLevel()+1);

        menuInfo.setMenuId(null);
        Long userId = jwtTokenUtil.getUserId();
        menuInfo.setCreateUserId(userId);
        menuInfo.setCreateTime(new Date());
        menuInfo.setMenuStatus(0);
        menuInfo.setUpdateUserId(null);
        menuInfo.setUpdateTime(null);
        menuInfoMapper.insert(menuInfo);
    }

    /**
     * 菜单状态变更
     * @param menuId
     * @param menuStatus
     */
    @Override
    public void updateMenuStatus(Long menuId, Integer menuStatus) {
        MenuInfo menuInfo = menuInfoMapper.selectById(menuId);
        if(menuInfo == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_MENU);
        }
        LambdaUpdateWrapper<MenuInfo> updateCondition = conditionFactory.getUpdateCondition(MenuInfo.class);
        updateCondition.eq(MenuInfo::getMenuId,menuId)
                .set(MenuInfo::getMenuStatus,menuStatus);
        this.update(updateCondition);
    }
}
