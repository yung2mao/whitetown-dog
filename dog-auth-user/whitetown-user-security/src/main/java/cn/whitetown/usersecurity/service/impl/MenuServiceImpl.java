package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.dogbase.common.entity.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.usersecurity.entity.po.MenuInfo;
import cn.whitetown.usersecurity.mappers.MenuInfoMapper;
import cn.whitetown.usersecurity.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 菜单管理服务
 * @author GrainRain
 * @date 2020/06/24 22:22
 **/
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
}
