package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.ao.RoleUserQuery;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.authcommon.entity.po.PositionInfo;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.po.UserRoleRelation;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.authcommon.util.defaultimpl.WhiteJwtTokenUtil;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.entity.WhiteLambdaQueryWrapper;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.dogbase.common.util.secret.Md5WithSaltUtil;
import cn.whitetown.authcommon.entity.ao.UserBasicQuery;
import cn.whitetown.authcommon.entity.dto.UserBasicInfoDto;
import cn.whitetown.usersecurity.manager.DeptManager;
import cn.whitetown.usersecurity.manager.PositionManager;
import cn.whitetown.usersecurity.manager.RoleManager;
import cn.whitetown.usersecurity.manager.UserManager;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.mappers.UserRoleRelationMapper;
import cn.whitetown.usersecurity.service.UserManageService;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户管理服务
 * @author GrainRain
 * @date 2020/06/17 20:48
 **/
@Service
public class UserManageServiceImpl extends ServiceImpl<UserBasicInfoMapper,UserBasicInfo> implements UserManageService {
    @Resource
    private UserBasicInfoMapper userMapper;

    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private AuthUserCacheUtil userCacheUtil;

    @Autowired
    private MenuCacheUtil menuCacheUtil;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private DeptManager deptManager;

    @Autowired
    private PositionManager positionManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private QueryConditionFactory queryConditionFactory;

    @Autowired
    private BeanTransFactory transFactory;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void addUserBasicInfo(String username, String password, String roleName) {
        UserBasicInfo userBasicInfo = userManager.getUserByUsername(username);
        //check username
        if(userBasicInfo != null){
            throw new CustomException(ResponseStatusEnum.USER_ALREADY_REG);
        }
        //check role
        UserRole userRole = roleManager.queryRoleByRoleName(roleName);
        if(userRole==null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        //create md5 password
        String salt = Md5WithSaltUtil.getRandomSalt();
        String md5Password = Md5WithSaltUtil.md5Encrypt(password,salt);
        //improve user info
        UserBasicInfo newUser = new UserBasicInfo();
        newUser.setUserId(null);
        newUser.setUsername(username);
        newUser.setPassword(md5Password);
        newUser.setSalt(salt);
        newUser.setUserStatus(DogBaseConstant.ACTIVE_NORMAL);
        newUser.setUserVersion(DogBaseConstant.INIT_VERSION);
        Long userId = jwtTokenUtil.getUserId();
        newUser.setCreateUserId(userId);
        newUser.setCreateTime(new Date());
        //insert into database
        userMapper.insert(newUser);
        //add role relation
        UserRoleRelation userRoleRelation = new UserRoleRelation(null,newUser.getUserId(),userRole.getRoleId());
        userRoleRelationMapper.insert(userRoleRelation);
    }

    @Override
    public ResponsePage<UserBasicInfoDto> queryUserBasicList(UserBasicQuery userQuery) {
        //粗粒度条件简单匹配 - 此处仅提供手机号或用户名
        if(!DataCheckUtil.checkTextNullBool(userQuery.getSearchDetail())){
            String detail = userQuery.getSearchDetail();
            //telephone
            if(detail.matches("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$")){
                userQuery.setTelephone(detail);
            }else {
                //username
                userQuery.setUsername(detail);
            }
        }
        //create condition
        LambdaQueryWrapper<UserBasicInfo> condition = queryConditionFactory.
                allEqWithNull2IsNull(userQuery, UserBasicInfo.class)
                .in(UserBasicInfo::getUserStatus,DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN);
        WhiteLambdaQueryWrapper<UserBasicInfo> whiteQueryWrapper = queryConditionFactory.createWhiteQueryWrapper(condition);
        condition = whiteQueryWrapper.between(UserBasicInfo::getCreateTime,userQuery.getStartTime(),userQuery.getEndTime(),false)
                .getLambdaQueryWrapper();
        //select data
        Page<UserBasicInfo> page = queryConditionFactory.createPage(userQuery.getPage(),userQuery.getSize(), UserBasicInfo.class);
        Page<UserBasicInfo> pageResult = userMapper.selectPage(page, condition);
        if(pageResult.getRecords()==null || pageResult.getRecords().size()==0){
            return new ResponsePage<>();
        }
        //to vo
        List<UserBasicInfoDto> userVos = pageResult.getRecords().stream()
                .map(us -> transFactory.trans(us, UserBasicInfoDto.class))
                .collect(Collectors.toList());

        return ResponsePage.createPage(pageResult.getCurrent(),
                pageResult.getSize(),
                pageResult.getTotal(),
                userVos);
    }

    @Override
    public ResponsePage<UserBasicInfoDto> queryUserByRoleId(RoleUserQuery roleUserQuery) {
        List<UserBasicInfo> users = userRoleRelationMapper.selectAllUserByRoleId(roleUserQuery.getRoleId());
        List<UserBasicInfoDto> userVos = users.stream()
                .map(us -> transFactory.trans(us, UserBasicInfoDto.class))
                .collect(Collectors.toList());
        return WhiteToolUtil.result2Page(userVos,roleUserQuery.getPage(),roleUserQuery.getSize());
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUser(UserBasicInfo userInfo) {
        UserBasicInfo user = userManager.getUserByUsername(userInfo.getUsername());
        if(user==null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
        }
        //判断deptId和positionId是否有变更,如果有变更则执行相应合法性校验
        boolean deptChange = false;
        boolean positionChange = false;
        if(userInfo.getDeptId() != null && !userInfo.getDeptId().equals(user.getDeptId())){
            deptChange = true;
        }
        if(userInfo.getDeptId() != null && userInfo.getPositionId() != null && !userInfo.getPositionId().equals(user.getPositionId())) {
            positionChange = true;
        }
        if(deptChange){
            DeptInfo deptInfo = deptManager.queryDeptInfoById(userInfo.getDeptId());
            if(deptInfo == null) {
                throw new CustomException(ResponseStatusEnum.NO_THIS_DEPT);
            }
            userInfo.setDeptName(deptInfo.getDeptName());
        }
        if(positionChange){
            PositionInfo positionInfo = positionManager.queryPositionByIdAndDeptId(userInfo.getDeptId(),userInfo.getPositionId());
            if(positionInfo == null) {
                throw new CustomException(ResponseStatusEnum.NO_THIS_POSITION);
            }
            //只允许一人的职位,检索当前职位是否已经有人
            if(positionInfo.getPositionLevel() == AuthConstant.ONE_PERSON_LEVEL) {
                LambdaQueryWrapper<UserBasicInfo> queryCondition = queryConditionFactory.getQueryCondition(UserBasicInfo.class);
                queryCondition.eq(UserBasicInfo::getDeptId,userInfo.getDeptId())
                        .eq(UserBasicInfo::getPositionId,positionInfo.getPositionId());
                List<UserBasicInfo> usList = userMapper.selectList(queryCondition);
                if(usList.size() > 0) {
                    throw new CustomException(ResponseStatusEnum.ONLY_ONE_PERSON_POSITION);
                }
            }
            userInfo.setPositionName(positionInfo.getPositionName());
        }
        //更新的信息处理
        if(userInfo.getDeptId() == null) {
            userInfo.setDeptName(null);
        }
        if(userInfo.getPositionId() == null) {
            userInfo.setPositionName(null);
        }
        userInfo.setUserId(user.getUserId());
        userInfo.setPassword(user.getPassword());
        userInfo.setSalt(user.getSalt());
        userInfo.setUserStatus(user.getUserStatus());
        userInfo.setUserVersion(user.getUserVersion());
        userInfo.setCreateUserId(user.getCreateUserId());
        userInfo.setCreateTime(user.getCreateTime());
        //update user
        Long userId = jwtTokenUtil.getUserId();
        userInfo.setUpdateUserId(userId);
        userInfo.setUpdateTime(new Date());
        //update
        userMapper.updateById(userInfo);
        if(positionChange || userInfo.getPositionId() != null) {
            deptManager.updatePositionInfo(userInfo.getPositionId(),userInfo.getUserId(),userInfo.getRealName());
        }
        //内存旧数据移除(如果存在)
        userCacheUtil.removeLoginUser(userInfo.getUsername());
    }

    @Override
    public void resetPassword(String username) {
        UserBasicInfo userBasicInfo = userManager.getUserByUsername(username);
        if(userBasicInfo==null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
        }
        Long updateUserId = jwtTokenUtil.getUserId();
        LambdaUpdateWrapper<UserBasicInfo> updateWrapper = new LambdaUpdateWrapper<>();
        String salt = Md5WithSaltUtil.getRandomSalt();
        String defaultPwd = Md5WithSaltUtil.md5Encrypt(AuthConstant.DEFAULT_PWD,salt);
        updateWrapper.eq(UserBasicInfo::getUsername,username)
                .set(UserBasicInfo::getPassword,defaultPwd)
                .set(UserBasicInfo::getSalt,salt)
                .set(UserBasicInfo::getUserVersion,userBasicInfo.getUserVersion()+1)
                .set(UserBasicInfo::getUpdateUserId,updateUserId)
                .set(UserBasicInfo::getUpdateTime,new Date());
        this.update(updateWrapper);
        userCacheUtil.removeUserDetails(username);
    }

    @Override
    public String checkPassword(String username,String oldPassword) {
        UserBasicInfo userBasicInfo = userManager.getUserByUsername(username);
        oldPassword = Md5WithSaltUtil.md5Encrypt(oldPassword,userBasicInfo.getSalt());
        if(!oldPassword.equals(userBasicInfo.getPassword())){
            throw new CustomException(ResponseStatusEnum.OLD_PWD_NOT_RIGHT);
        }
        Map<String,Object> tokenMap = new HashMap<>(2);
        tokenMap.put(WhiteJwtTokenUtil.USERNAME,username);
        tokenMap.put(AuthConstant.PWD_TOKEN_TIME,System.currentTimeMillis());
        String tokenByParams = jwtTokenUtil.createTokenByParams(tokenMap);
        return tokenByParams;
    }

    @Override
    public void updatePassword(String username, String pwdToken, String newPassword) {
        Claims claims = jwtTokenUtil.readTokenAsMapParams(pwdToken);
        String pwdUsername = claims.get(WhiteJwtTokenUtil.USERNAME,String.class);
        Long pwdTokenTime = claims.get(AuthConstant.PWD_TOKEN_TIME,Long.class);
        if(!username.equals(pwdUsername)){
            throw new CustomException(ResponseStatusEnum.ERROR_PARAMS);
        }
        if(System.currentTimeMillis()-pwdTokenTime > AuthConstant.PWD_TOKEN_EXPIRE_TIME){
            throw new CustomException(ResponseStatusEnum.CHECK_EXPIRE);
        }
        String randomSalt = Md5WithSaltUtil.getRandomSalt();
        newPassword = Md5WithSaltUtil.md5Encrypt(newPassword,randomSalt);
        LambdaUpdateWrapper<UserBasicInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserBasicInfo::getUsername,username)
                .set(UserBasicInfo::getPassword,newPassword)
                .set(UserBasicInfo::getSalt,randomSalt);
        this.update(updateWrapper);
        userCacheUtil.removeUserDetails(username);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void changeUserStatus(String username, Integer userStatus) {
        UserBasicInfo userBasicInfo = userManager.getUserByUsername(username);
        if(userBasicInfo == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
        }
        LambdaUpdateWrapper<UserBasicInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserBasicInfo::getUsername,username)
                .set(UserBasicInfo::getUserStatus,userStatus);
        this.update(updateWrapper);
        if(userStatus == DogBaseConstant.DELETE_ERROR){
            //删除状态，同步移除用户关联信息
            userRoleRelationMapper.removeUserRelationInfo(userBasicInfo.getUserId());
        }
        userCacheUtil.removeAllUserCacheInfo(username);
        menuCacheUtil.removeCacheMenuList(userBasicInfo.getUserId());
    }
}
