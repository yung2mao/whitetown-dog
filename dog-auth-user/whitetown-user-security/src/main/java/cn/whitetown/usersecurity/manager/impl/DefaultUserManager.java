package cn.whitetown.usersecurity.manager.impl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.UserRoleRelation;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.UserManager;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.mappers.UserRoleRelationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户管理实现类
 * @author GrainRain
 * @date 2020/07/07 21:56
 **/
@Service
public class DefaultUserManager implements UserManager {

    @Autowired
    private QueryConditionFactory conditionFactory;

    @Resource
    private UserBasicInfoMapper userMapper;

    @Override
    public UserBasicInfo getUserByUsername(String username) {
        LambdaUpdateWrapper<UserBasicInfo> condition = conditionFactory.getUpdateCondition(UserBasicInfo.class);
        condition.eq(UserBasicInfo::getUsername,username)
                .in(UserBasicInfo::getUserStatus,0,1);
        return userMapper.selectOne(condition);
    }
}