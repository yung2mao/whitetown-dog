package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.vo.RoleInfoVo;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.mappers.RoleInfoMapper;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.service.RoleManageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理服务
 * @author GrainRain
 * @date 2020/06/28 21:54
 **/
@Service
public class RoleManageServiceImpl extends ServiceImpl<RoleInfoMapper, UserRole> implements RoleManageService {

    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Resource
    private UserBasicInfoMapper userMapper;

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

        return this.roleInfo2Vo(roleList);
    }

    /**
     * 查询指定用户的全部角色信息
     * @param username
     * @return
     */
    @Override
    public List<RoleInfoVo> queryRolesByUsername(String username) {
        LambdaQueryWrapper<UserBasicInfo> queryWrapper = queryConditionFactory.getLambdaCondition(UserBasicInfo.class);
        queryWrapper.eq(UserBasicInfo::getUsername,username)
                .select(UserBasicInfo::getUserId);
        UserBasicInfo userBasicInfo = userMapper.selectOne(queryWrapper);
        if(userBasicInfo == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
        }

        List<UserRole> roles = roleInfoMapper.selectRolesByUsername(username);
        return this.roleInfo2Vo(roles);
    }

    /**
     * 添加角色
     * @param role
     */
    @Override
    public void addRole(RoleInfoVo role){
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getName,role.getName());
        UserRole ro = this.getOne(queryWrapper);
        if(ro != null){
            throw new CustomException(ResponseStatusEnum.ROLE_EXISTS);
        }
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
     * 角色名称禁止变更
     * @param role
     */
    @Override
    public void updateRoleInfo(RoleInfoVo role) {
        UserRole oldRole = this.getOneByRoleId(role.getRoleId());
        if(oldRole == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }

        Long updateUserId = jwtTokenUtil.getUserId();
        if(role.getSort() != null && role.getSort() > 0){
            oldRole.setSort(role.getSort());
        }
        oldRole.setDescription(role.getDescription());
        oldRole.setVersion(oldRole.getVersion()+1);
        oldRole.setUpdateUserId(updateUserId);
        oldRole.setUpdateTime(new Date());
        this.updateById(oldRole);
    }

    @Override
    public void updateRoleStatus(Long roleId, Integer roleStatus) {
        UserRole role = this.getOneByRoleId(roleId);
        if(role == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getRoleId,roleId)
                .set(UserRole::getRoleStatus,roleStatus);
        this.update(updateWrapper);
        //内存数据处理
        if(roleStatus != 0){

        }
    }

    /**
     * 将UserRole List转换为返回前端的数据对象
     * @param roleList
     * @return
     */
    private List<RoleInfoVo> roleInfo2Vo(List<UserRole> roleList){
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
     * 根据角色ID查询一个角色信息
     * @param roleId
     * @return
     */
    private UserRole getOneByRoleId(Long roleId){
        LambdaQueryWrapper<UserRole> lambdaCondition = queryConditionFactory.getLambdaCondition(UserRole.class);
        lambdaCondition.eq(UserRole::getRoleId,roleId)
                .in(UserRole::getRoleStatus,0,1);
        return this.getOne(lambdaCondition);
    }

}
