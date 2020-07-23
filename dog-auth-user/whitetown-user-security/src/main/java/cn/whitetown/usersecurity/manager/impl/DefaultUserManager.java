package cn.whitetown.usersecurity.manager.impl;

import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.UserManager;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
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
                .in(UserBasicInfo::getUserStatus, DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN);
        return userMapper.selectOne(condition);
    }

    @Override
    public List<UserBasicInfo> getUserByWrapper(LambdaQueryWrapper<UserBasicInfo> queryWrapper) {
        if(queryWrapper == null) {
            return new ArrayList<>();
        }
        return userMapper.selectList(queryWrapper);
    }
}
