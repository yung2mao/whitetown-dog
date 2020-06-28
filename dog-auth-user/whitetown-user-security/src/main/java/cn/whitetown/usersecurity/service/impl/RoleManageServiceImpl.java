package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.vo.RoleInfoVo;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.mappers.RoleInfoMapper;
import cn.whitetown.usersecurity.service.RoleManageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GrainRain
 * @date 2020/06/28 21:54
 **/
@Service
public class RoleManageServiceImpl extends ServiceImpl<RoleInfoMapper, UserRole> implements RoleManageService {

    @Autowired
    private BeanTransFactory transUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private QueryConditionFactory queryConditionFactory;

    /**
     * 查询所有角色信息
     * @return
     */
    @Override
    public List<RoleInfoVo> queryAllRoles() {
        //condition
        LambdaQueryWrapper<UserRole> queryWrapper = queryConditionFactory.getLambdaCondition(UserRole.class);
        queryWrapper.in(UserRole::getRoleStatus,0,1)
                .orderByAsc(UserRole::getSort);
        //query
        List<UserRole> roleList = this.list(queryWrapper);
        List<RoleInfoVo> roleInfoVoList = roleList.stream().map(userRole -> {
            try {
                return transUtil.trans(userRole, RoleInfoVo.class);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return roleInfoVoList;
    }

    /**
     * 添加角色
     * @param role
     */
    @Override
    public void addRole(RoleInfoVo role){
        UserRole userRole = null;
        try {
            userRole = transUtil.trans(role, UserRole.class);
        } catch (Exception e) {
            throw new CustomException(ResponseStatusEnum.SERVER_ERROR);
        }
        Long createUserId = jwtTokenUtil.getUserId();
        if(role.getSort() == null){
            userRole.setSort(100);
        }
        userRole.setRoleId(null);
        userRole.setRoleStatus(0);
        userRole.setVersion(0);
        userRole.setCreateUserId(createUserId);
        userRole.setCreateTime(new Date());
        this.save(userRole);
    }

    /**
     * 角色信息更新
     * @param role
     */
    @Override
    public void updateRoleInfo(RoleInfoVo role) {
        LambdaQueryWrapper<UserRole> lambdaCondition = queryConditionFactory.getLambdaCondition(UserRole.class);
        lambdaCondition.eq(UserRole::getRoleId,role.getRoleId());
        UserRole oldRole = this.getOne(lambdaCondition);
        if(oldRole == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        Long updateUserId = jwtTokenUtil.getUserId();
        if(role.getSort() != null && role.getSort() > 0){
            oldRole.setSort(role.getSort());
        }
        oldRole.setName(role.getName());
        oldRole.setDescription(role.getDescription());
        oldRole.setVersion(oldRole.getVersion()+1);
        oldRole.setUpdateUserId(updateUserId);
        oldRole.setUpdateTime(new Date());
        this.updateById(oldRole);
    }

}
