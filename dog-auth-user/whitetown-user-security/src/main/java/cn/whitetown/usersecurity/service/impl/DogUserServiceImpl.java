package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.UserRole;
import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authcommon.util.captcha.CaptchaDataDeal;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.authcommon.entity.vo.LoginUser;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.entity.WhiteLambdaQueryWrapper;
import cn.whitetown.dogbase.common.util.secret.Md5WithSaltUtil;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.service.DogUserService;
import cn.whitetown.usersecurity.util.LoginUserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户服务
 * @author GrainRain
 * @date 2020/06/13 15:00
 **/
@Service
public class DogUserServiceImpl extends ServiceImpl<UserBasicInfoMapper,UserBasicInfo> implements DogUserService {

    @Autowired
    private CaptchaDataDeal captchaDataDeal;

    @Resource
    private UserBasicInfoMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserCacheUtil userCacheUtil;

    /**
     * 验证码校验
     * @param captcha 验证码
     * @param clientIp 客户ip地址
     */
    @Override
    public void checkCaptcha(String captcha, String clientIp) {
        if(DataCheckUtil.checkTextNullBool(captcha)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
        String saveIp = captchaDataDeal.getCaptcha(captcha.toLowerCase());
        if(DataCheckUtil.checkTextNullBool(saveIp)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
        //校验两次IP是否相同，由此判断是否同一用户
        if(!saveIp.equalsIgnoreCase(clientIp)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
    }

    /**
     * 用户名密码校验
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public String checkUserNameAndPassword(String username, String password) {
        if(DataCheckUtil.checkTextNullBool(username) || DataCheckUtil.checkTextNullBool(password)){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        UserBasicInfo user = this.selectUserByUsername(username);
        if(user==null){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        if(user.getUserStatus()==1){
            throw new CustomException(ResponseStatusEnum.ACCOUNT_FREEZED);
        }
        String salt = user.getSalt();
        String md5WithSalt = Md5WithSaltUtil.md5Encrypt(password,salt);

        if(!user.getPassword().equals(md5WithSalt)){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }

        //角色信息
        List<UserRole> roles = userMapper.selectUserRole(user.getUserId());
        if(roles==null || roles.size()==0){
            throw new CustomException(ResponseStatusEnum.NO_PERMITION);
        }
        LoginUser loginUser = LoginUserUtil.getLoginUser(user,roles);
        //create token
        //存储信息包括userId，username,roles,userVersion
        Map<String,Object> map = new HashMap<>(10);
        map.put(JwtTokenUtil.USER_ID,user.getUserId());
        map.put(JwtTokenUtil.USERNAME,username);
        map.put(JwtTokenUtil.USER_ROLE,loginUser.getRoles());
        map.put(JwtTokenUtil.USER_VERSION,user.getUserVersion());
        String token = jwtTokenUtil.createTokenByParams(map);

        //存放登录用户的信息，方便用户获取使用,存储时间为2小时
        userCacheUtil.saveUserBasicInfo(loginUser.getUsername(),loginUser);

        //存储用户校验使用的信息在内存中,方便快速校验
        Collection<GrantedAuthority> roleCollection = LoginUserUtil.createRoleCollection(loginUser.getRoles());
        UserDetails userDetails = new User(user.getUsername(),user.getPassword(),roleCollection);
        userCacheUtil.saveUserDetail(AuthConstant.USERDETAIL_PREFIX+userDetails.getUsername(),
                userDetails);
        return token;
    }

    /**
     * 根据token获取用户信息
     * @return
     */
    @Override
    public LoginUser getUserByToken() {
        String username = jwtTokenUtil.getUsername();
        LoginUser user = userCacheUtil.getUserBasicInfo(username);
        if(user ==null){
            UserBasicInfo userBasic = this.selectUserByUsername(username);
            if(userBasic==null){
                throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            user = LoginUserUtil.getLoginUser(userBasic,null);
            //save to memory
            userCacheUtil.saveUserBasicInfo(user.getUsername(),user);
        }
        return user;
    }

    /**
     * 退出登录
     * 基于版本号更新使原有token彻底失效，配合前端销毁token处理
     */
    @Override
    public void logout() {
        String username = jwtTokenUtil.getUsername();
        if(username==null){
            return;
        }
        userCacheUtil.removeLoginUser(username);
        userCacheUtil.removeUserDetails(AuthConstant.USERDETAIL_PREFIX+username);
        WhiteLambdaQueryWrapper<UserBasicInfo> queryCondition = new WhiteLambdaQueryWrapper<>();
        queryCondition.select(UserBasicInfo::getUserVersion);
        queryCondition.eq(UserBasicInfo::getUsername,username);
        UserBasicInfo userBasicInfo = userMapper.selectOne(queryCondition);
        Integer newVersion = userBasicInfo.getUserVersion()+1;
        LambdaUpdateWrapper<UserBasicInfo> updateCondition = new LambdaUpdateWrapper<>();
        updateCondition.eq(UserBasicInfo::getUsername,username);
        updateCondition.set(UserBasicInfo::getUserVersion,newVersion);
        this.update(updateCondition);
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
