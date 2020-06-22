package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.dogbase.common.entity.vo.ResponsePage;
import cn.whitetown.dogbase.common.entity.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.dogbase.user.entity.vo.LoginUser;
import cn.whitetown.dogbase.user.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.user.entity.UserRole;
import cn.whitetown.dogbase.user.entity.UserRoleRelation;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.dogbase.user.util.UserCacheUtil;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import cn.whitetown.dogbase.db.entity.WhiteLambdaQueryWrapper;
import cn.whitetown.dogbase.common.util.secret.Md5WithSaltUtil;
import cn.whitetown.usersecurity.entity.ao.UserBasicQuery;
import cn.whitetown.usersecurity.entity.vo.UserBasicInfoVo;
import cn.whitetown.usersecurity.mappers.RoleInfoMapper;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.mappers.UserRoleRelationMapper;
import cn.whitetown.usersecurity.service.UserManageService;
import cn.whitetown.usersecurity.util.LoginUserUtil;
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
    private SnowIDCreateUtil idCreateUtil;

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
        LambdaQueryWrapper<UserBasicInfo> condition = queryConditionFactory.
                allEqWithNull2IsNull(userQuery, UserBasicInfo.class);
        condition.in(UserBasicInfo::getUserStatus,0,1);
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
        roleCondition.eq(UserRole::getName,roleName);
        UserRole userRole = roleInfoMapper.selectOne(roleCondition);
        if(userRole==null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_ROLE);
        }
        //create md5 password
        String salt = Md5WithSaltUtil.getRandomSalt();
        String md5Password = Md5WithSaltUtil.md5Encrypt(password,salt);
        //improve user info
        UserBasicInfo newUser = new UserBasicInfo();
        newUser.setUserId(idCreateUtil.getSnowId());
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
        UserRoleRelation userRoleRelation = new UserRoleRelation(idCreateUtil.getSnowId(),newUser.getUserId(),userRole.getRoleId());
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
        //如果是用户本人修改，对内存数据做同步更新
        if(userId.equals(newUser.getUserId())){
            LoginUser loginUser = LoginUserUtil.getLoginUser(newUser, null);
            userCacheUtil.saveUserBasicInfo(loginUser.getUsername(),loginUser);
        }
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
}
