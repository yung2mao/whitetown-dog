package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.ao.UserRoleConfigure;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.vo.RoleInfoVo;
import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.RoleManager;
import cn.whitetown.usersecurity.manager.UserManager;
import cn.whitetown.usersecurity.mappers.RoleInfoMapper;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.mappers.UserRoleRelationMapper;
import cn.whitetown.usersecurity.service.RoleManageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
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
    private UserRoleRelationMapper roleRelationMapper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserCacheUtil userCacheUtil;

    @Autowired
    private RoleManager roleManager;

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
        LambdaQueryWrapper<UserRole> queryWrapper = queryConditionFactory.getQueryCondition(UserRole.class);
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
        UserBasicInfo userBasicInfo = userManager.getUserByUsername(username);
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
        userRole = transUtil.trans(role, UserRole.class);
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
        UserRole oldRole = roleManager.queryRoleById(role.getRoleId());
        if(oldRole == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        if(!oldRole.getName().equalsIgnoreCase(role.getName())){
            throw new CustomException(ResponseStatusEnum.REQUEST_INVALIDATE);
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

    /**
     * 角色状态变更
     * @param roleId
     * @param roleStatus
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateRoleStatus(Long roleId, Integer roleStatus) {
        UserRole role = roleManager.queryRoleById(roleId);
        if(role == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getRoleId,roleId)
                .set(UserRole::getRoleStatus,roleStatus);
        this.update(updateWrapper);

        if(roleStatus == 2){
            //角色删除，对应关联关系一并删除
            roleRelationMapper.removeRoleRelation(roleId);
        }
    }

    /**
     * 用户角色分配
     * @param roleConfigure
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUserRoleRelation(UserRoleConfigure roleConfigure) {
        UserBasicInfo userBasicInfo = userManager.getUserByUsername(roleConfigure.getUsername());
        if(userBasicInfo == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
        }
        List<Long> roleIds = Arrays.asList(roleConfigure.getRoleIds());
        roleRelationMapper.updateUserRoleRelation(userBasicInfo.getUserId(),roleIds);
        userCacheUtil.removeUserDetails(AuthConstant.USERDETAIL_PREFIX+roleConfigure.getUsername());
    }

    /**
     * 将UserRole List转换为返回前端的数据对象
     * @param roleList
     * @return
     */
    private List<RoleInfoVo> roleInfo2Vo(List<UserRole> roleList){
        List<RoleInfoVo> roleInfoVoList = roleList.stream()
                .map(userRole -> transUtil.trans(userRole, RoleInfoVo.class))
                .collect(Collectors.toList());
        return roleInfoVoList;
    }
}
