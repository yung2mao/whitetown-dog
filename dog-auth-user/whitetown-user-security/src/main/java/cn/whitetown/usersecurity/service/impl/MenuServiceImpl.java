package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.util.MenuUtil;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.vo.MenuTree;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.mappers.MenuInfoMapper;
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
     * 添加菜单信息
     * @param menuInfo
     */
    @Override
    public void addSingleMenu(MenuInfo menuInfo) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuInfo::getMenuCode,menuInfo.getMenuCode());
        MenuInfo oldMenu = menuInfoMapper.selectOne(queryWrapper);
        if(oldMenu != null){
            throw new CustomException(ResponseStatusEnum.EXISTED_THE_MENU);
        }
        Long userId = jwtTokenUtil.getUserId();
        menuInfo.setCreateUserId(userId);
        menuInfo.setCreateTime(new Date());
        menuInfo.setMenuStatus(0);
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
