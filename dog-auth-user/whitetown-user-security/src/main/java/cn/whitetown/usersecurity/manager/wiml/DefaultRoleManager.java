package cn.whitetown.usersecurity.manager.wiml;

import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.RoleManager;
import cn.whitetown.usersecurity.mappers.RoleInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author GrainRain
 * @date 2020/07/07 22:11
 **/
@Service
public class DefaultRoleManager implements RoleManager {

    @Autowired
    private QueryConditionFactory conditionFactory;

    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Override
    public UserRole queryRoleById(Long roleId) {
        LambdaQueryWrapper<UserRole> queryWrapper = conditionFactory.getQueryCondition(UserRole.class);
        queryWrapper.eq(UserRole::getRoleId,roleId)
                .in(UserRole::getRoleStatus, DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN);
        return roleInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public UserRole queryRoleByRoleName(String roleName) {
        LambdaQueryWrapper<UserRole> queryWrapper = conditionFactory.getQueryCondition(UserRole.class);
        queryWrapper.eq(UserRole::getName,roleName)
                .in(UserRole::getRoleStatus,DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN);
        UserRole userRole = roleInfoMapper.selectOne(queryWrapper);
        return userRole;
    }

    @Override
    public List<UserRole> queryRolesByUserId(Long userId) {
        return roleInfoMapper.selectRolesByUserId(userId);
    }
}
