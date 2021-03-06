package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.ao.RoleQuery;
import cn.whitetown.authcommon.entity.ao.UserRoleConfigure;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.dto.RoleInfoDto;
import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.RoleManager;
import cn.whitetown.usersecurity.manager.UserManager;
import cn.whitetown.usersecurity.mappers.RoleInfoMapper;
import cn.whitetown.usersecurity.mappers.UserRoleRelationMapper;
import cn.whitetown.usersecurity.service.RoleManageService;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Comparator;
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
    private UserRoleRelationMapper userRoleRelationMapper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private AuthUserCacheUtil authUserCacheUtil;

    @Autowired
    private MenuCacheUtil menuCacheUtil;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private BeanTransFactory transUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private QueryConditionFactory queryConditionFactory;

    @Override
    public List<RoleInfoDto> queryAllRoles() {
        //condition
        LambdaQueryWrapper<UserRole> queryWrapper = queryConditionFactory.getQueryCondition(UserRole.class);
        queryWrapper.in(UserRole::getRoleStatus, DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN);
        //query
        List<UserRole> roleList = this.list(queryWrapper);
        roleList = roleList.stream().sorted(Comparator.comparing(UserRole::getSort)).collect(Collectors.toList());

        return this.roleInfo2Vo(roleList);
    }

    @Override
    public List<RoleInfoDto> queryRolesByUsername(String username) {
        UserBasicInfo userBasicInfo = userManager.getUserByUsername(username);
        if(userBasicInfo == null){
            throw new WhResException(ResponseStatusEnum.NO_THIS_USER);
        }

        List<UserRole> roles = roleInfoMapper.selectRolesByUsername(username);
        return this.roleInfo2Vo(roles);
    }

    @Override
    public List<RoleInfoDto> searchRole(RoleQuery roleQuery) {
        if(!DataCheckUtil.checkTextNullBool(roleQuery.getDetail())){
            String detail = roleQuery.getDetail();
            String nameRegex = "[a-zA-Z_]+";
            if(detail.matches(nameRegex)){
                roleQuery.setRoleName(detail);
            }else {
                roleQuery.setDescription(detail);
            }
        }
        LambdaQueryWrapper<UserRole> queryWrapper = queryConditionFactory.allEqWithNull2IsNull(roleQuery, UserRole.class);
        List<UserRole> userRoles = roleInfoMapper.selectList(queryWrapper);
        return this.roleInfo2Vo(userRoles);
    }

    @Override
    public void addRole(RoleInfoDto role){
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getName,role.getName());
        UserRole ro = this.getOne(queryWrapper);
        if(ro != null){
            throw new WhResException(ResponseStatusEnum.ROLE_EXISTS);
        }
        UserRole userRole = null;
        userRole = transUtil.trans(role, UserRole.class);
        Long createUserId = jwtTokenUtil.getUserId();
        if(role.getSort() == null){
            userRole.setSort(AuthConstant.MENU_MAX_SORT);
        }
        userRole.setRoleId(null);
        userRole.setRoleStatus(DogBaseConstant.ACTIVE_NORMAL);
        userRole.setVersion(DogBaseConstant.INIT_VERSION);
        userRole.setCreateUserId(createUserId);
        userRole.setCreateTime(new Date());
        this.save(userRole);
    }

    /**
     * 角色更新 - name禁止更新
     * @param role
     */
    @Override
    public void updateRoleInfo(RoleInfoDto role) {
        UserRole oldRole = roleManager.queryRoleById(role.getRoleId());
        if(oldRole == null){
            throw new WhResException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        if(!oldRole.getName().equalsIgnoreCase(role.getName())){
            throw new WhResException(ResponseStatusEnum.REQUEST_INVALIDATE);
        }

        Long updateUserId = jwtTokenUtil.getUserId();
        if(role.getSort() != null && role.getSort() > 0 && role.getSort() <= AuthConstant.MENU_MAX_SORT){
            oldRole.setSort(role.getSort());
        }
        oldRole.setDescription(role.getDescription());
        oldRole.setVersion(oldRole.getVersion()+1);
        oldRole.setUpdateUserId(updateUserId);
        oldRole.setUpdateTime(new Date());
        this.updateById(oldRole);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateRoleStatus(Long roleId, Integer roleStatus) {
        UserRole role = roleManager.queryRoleById(roleId);
        if(role == null){
            throw new WhResException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        LambdaUpdateWrapper<UserRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getRoleId,roleId)
                .set(UserRole::getRoleStatus,roleStatus);
        this.update(updateWrapper);

        if(roleStatus == DogBaseConstant.DELETE_ERROR){
            //角色删除，对应关联关系一并删除
            userRoleRelationMapper.removeRoleRelation(roleId);
        }
        authUserCacheUtil.clearAllUsersAuthors();
        menuCacheUtil.reset();

    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUserRoleRelation(UserRoleConfigure roleConfigure) {
        UserBasicInfo userBasicInfo = userManager.getUserByUsername(roleConfigure.getUsername());
        if(userBasicInfo == null){
            throw new WhResException(ResponseStatusEnum.NO_THIS_USER);
        }
        List<Long> roleIds = Arrays.asList(roleConfigure.getRoleIds());
        userRoleRelationMapper.updateUserRoleRelation(userBasicInfo.getUserId(),roleIds);
        menuCacheUtil.removeCacheMenuList(userBasicInfo.getUserId());
        authUserCacheUtil.removeAllUserCacheInfo(userBasicInfo.getUsername());
    }

    /**
     * 将UserRole List转换为返回前端的数据对象
     * @param roleList
     * @return
     */
    private List<RoleInfoDto> roleInfo2Vo(List<UserRole> roleList){
        List<RoleInfoDto> roleInfoDtoList = roleList.stream()
                .map(userRole -> transUtil.trans(userRole, RoleInfoDto.class))
                .collect(Collectors.toList());
        return roleInfoDtoList;
    }
}
