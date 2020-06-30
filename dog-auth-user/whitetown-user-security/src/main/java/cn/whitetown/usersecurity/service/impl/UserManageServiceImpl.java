package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.UserRoleRelation;
import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.vo.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.dogbase.db.entity.WhiteLambdaQueryWrapper;
import cn.whitetown.dogbase.common.util.secret.Md5WithSaltUtil;
import cn.whitetown.authcommon.entity.ao.UserBasicQuery;
import cn.whitetown.authcommon.entity.vo.UserBasicInfoVo;
import cn.whitetown.usersecurity.mappers.RoleInfoMapper;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.mappers.UserRoleRelationMapper;
import cn.whitetown.usersecurity.service.UserManageService;
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
    private RoleInfoMapper roleInfoMapper;

    @Resource
    private UserRoleRelationMapper userAndRoleMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserCacheUtil userCacheUtil;

    @Autowired
    private QueryConditionFactory queryConditionFactory;

    @Autowired
    private BeanTransFactory transFactory;

    /**
     * 根据条件进行分页查询用户基本信息
     * @param userQuery
     * @return
     */
    @Override
    public ResponsePage<UserBasicInfoVo> queryUserBasicList(UserBasicQuery userQuery) {
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
                .in(UserBasicInfo::getUserStatus,0,1);
        if(!DataCheckUtil.checkTextNullBool(userQuery.getStartTime())){
            condition.ge(UserBasicInfo::getCreateTime,userQuery.getStartTime());
        }
        if(!DataCheckUtil.checkTextNullBool(userQuery.getEndTime())){
            condition.le(UserBasicInfo::getCreateTime,userQuery.getEndTime());
        }
        //select data
        Page<UserBasicInfo> page = queryConditionFactory.createPage(userQuery, UserBasicInfo.class);
        Page<UserBasicInfo> pageResult = userMapper.selectPage(page, condition);
        if(pageResult.getRecords()==null || pageResult.getRecords().size()==0){
            return null;
        }
        //to vo
        List<UserBasicInfoVo> userVos = pageResult.getRecords().stream().map(us -> {
            UserBasicInfoVo userBasicInfoVo = null;
            try {
                userBasicInfoVo = transFactory.trans(us, UserBasicInfoVo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return userBasicInfoVo;
        }).collect(Collectors.toList());

        return ResponsePage.createPage(pageResult.getCurrent(),
                pageResult.getSize(),
                pageResult.getTotal(),
                userVos);
    }

    /**
     * 添加用户基础信息
     * 可用于注册操作或分配用户操作
     * @param username
     * @param password
     * @param roleName
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void addUserBasicInfo(String username, String password, String roleName) {
        UserBasicInfo userBasicInfo = this.selectUserByUsername(username);
        //check username
        if(userBasicInfo != null){
            throw new CustomException(ResponseStatusEnum.USER_ALREADY_REG);
        }
        //check role
        WhiteLambdaQueryWrapper<UserRole> roleCondition = new WhiteLambdaQueryWrapper<>();
        roleCondition.eq(UserRole::getName,roleName)
                .eq(UserRole::getRoleStatus,0);
        UserRole userRole = roleInfoMapper.selectOne(roleCondition);
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
        newUser.setUserStatus(0);
        newUser.setUserVersion(0);
        Long userId = jwtTokenUtil.getUserId();
        newUser.setCreateUserId(userId);
        newUser.setCreateTime(new Date());
        //insert into database
        userMapper.insert(newUser);
        //add role relation
        UserRoleRelation userRoleRelation = new UserRoleRelation(null,newUser.getUserId(),userRole.getRoleId());
        userAndRoleMapper.insert(userRoleRelation);
    }

    /**
     * 用户信息更新
     * @param userInfo
     */
    @Override
    public void updateUser(UserBasicInfo userInfo) {
        UserBasicInfo user = this.selectUserByUsername(userInfo.getUsername());
        if(user==null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
        }
        //更新的信息处理
        UserBasicInfo newUser = (UserBasicInfo) WhiteToolUtil.mergeObject(user, userInfo);
        newUser.setUserId(user.getUserId());
        newUser.setPassword(user.getPassword());
        newUser.setSalt(user.getSalt());
        newUser.setUserStatus(user.getUserStatus());
        newUser.setUserVersion(user.getUserVersion());
        newUser.setCreateUserId(user.getCreateUserId());
        newUser.setCreateTime(user.getCreateTime());
        //update user
        Long userId = jwtTokenUtil.getUserId();
        newUser.setUpdateUserId(userId);
        newUser.setUpdateTime(new Date());
        //update
        userMapper.updateById(newUser);

        //内存旧数据移除(如果存在)
        this.removeCacheUser(newUser.getUsername());
    }

    /**
     * 密码重置操作
     * @param username
     */
    @Override
    public void retryPassword(String username) {
        UserBasicInfo userBasicInfo = this.selectUserByUsername(username);
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
                .set(UserBasicInfo::getUpdateUserId,updateUserId)
                .set(UserBasicInfo::getUpdateTime,new Date());
        this.update(updateWrapper);

        this.removeCacheUser(username);
    }

    /**
     * 旧有密码校验
     * @param username
     * @param oldPassword
     * @return
     */
    @Override
    public String checkPassword(String username,String oldPassword) {
        UserBasicInfo userBasicInfo = this.selectUserByUsername(username);
        oldPassword = Md5WithSaltUtil.md5Encrypt(oldPassword,userBasicInfo.getSalt());
        if(!oldPassword.equals(userBasicInfo.getPassword())){
            throw new CustomException(ResponseStatusEnum.OLD_PWD_NOT_RIGHT);
        }
        Map<String,Object> tokenMap = new HashMap<>(1);
        tokenMap.put(JwtTokenUtil.USERNAME,username);
        tokenMap.put(AuthConstant.PWD_TOKEN_TIME,System.currentTimeMillis());
        String tokenByParams = jwtTokenUtil.createTokenByParams(tokenMap);
        return tokenByParams;
    }

    /**
     * 密码变更
     * @param username
     * @param pwdToken
     * @param newPassword
     */
    @Override
    public void updatePassword(String username, String pwdToken, String newPassword) {
        Claims claims = jwtTokenUtil.readTokenAsMapParams(pwdToken);
        String pwdUsername = claims.get(JwtTokenUtil.USERNAME,String.class);
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
        //如果内存中保留了一条相同数据，那么做同步更新
        UserDetails userDetails = userCacheUtil.getUserDetails(AuthConstant.USERDETAIL_PREFIX + username);
        if(userDetails != null){
            UserDetails newUserDetails = new User(username,newPassword,userDetails.getAuthorities());
            userCacheUtil.saveUserDetail(AuthConstant.USERDETAIL_PREFIX+username,newUserDetails);
        }
    }

    @Override
    public void changeUserStatus(String username, Integer userStatus) {
        UserBasicInfo userBasicInfo = this.selectUserByUsername(username);
        if(userBasicInfo == null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
        }
        LambdaUpdateWrapper<UserBasicInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserBasicInfo::getUsername,username)
                .set(UserBasicInfo::getUserStatus,userStatus);
        this.update(updateWrapper);
        this.removeCacheUser(username);
    }

    /**
     * 通过用户名搜索用户信息
     * @param username
     * @return
     */
    private UserBasicInfo selectUserByUsername(String username){
        LambdaQueryWrapper<UserBasicInfo> condition = new LambdaQueryWrapper<>();
        condition.eq(UserBasicInfo::getUsername,username)
                .in(UserBasicInfo::getUserStatus,0,1);
        return userMapper.selectOne(condition);
    }

    /**
     * 从内存移除username匹配的旧数据
     * @param username
     */
    private void removeCacheUser(String username){
        userCacheUtil.removeUserInfo(username,AuthConstant.USERDETAIL_PREFIX+username);
    }
}
