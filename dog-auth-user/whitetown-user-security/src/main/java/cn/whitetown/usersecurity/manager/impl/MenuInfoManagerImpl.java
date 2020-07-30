package cn.whitetown.usersecurity.manager.impl;

import cn.whitetown.authcommon.entity.dto.MenuTree;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.usersecurity.manager.MenuInfoManager;
import cn.whitetown.usersecurity.mappers.MenuInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author taixian
 * @date 2020/07/30
 **/
@Service
public class MenuInfoManagerImpl implements MenuInfoManager {

    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Override
    public List<MenuInfo> queryActiveMenuByUserId(Long userId, Long menuId, Integer lowLevel) {
        return menuInfoMapper.selectActiveMenuByUserId(DogBaseConstant.ACTIVE_NORMAL,userId,menuId,lowLevel);
    }
}
