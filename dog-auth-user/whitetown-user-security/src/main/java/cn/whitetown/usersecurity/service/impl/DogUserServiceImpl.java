package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authcommon.util.captcha.CaptchaDataDeal;
import cn.whitetown.authcommon.util.defaultimpl.WhiteJwtTokenUtil;
import cn.whitetown.authea.util.AuthCacheUtil;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.authcommon.entity.dto.LoginUser;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.common.util.secret.Md5WithSaltUtil;
import cn.whitetown.usersecurity.manager.RoleManager;
import cn.whitetown.usersecurity.manager.UserManager;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.service.DogUserService;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import cn.whitetown.usersecurity.util.LoginUserUtil;
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
    private UserManager userManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthUserCacheUtil userCacheUtil;

    @Autowired
    private AuthCacheUtil authCacheUtil;

    /**
     * 验证码校验
     * @param captcha 验证码
     * @param sessionId
     */
    @Override
    public void checkCaptcha(String captcha, String sessionId) {
        if(DataCheckUtil.checkTextNullBool(captcha)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
        String realCaptcha = captchaDataDeal.getCaptcha(sessionId);
        if(DataCheckUtil.checkTextNullBool(realCaptcha)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
        //校验存储的验证码和传入值是否相同
        if(!captcha.equalsIgnoreCase(realCaptcha)){
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
        UserBasicInfo user = userManager.getUserByUsername(username);
        if(user==null){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        if(user.getUserStatus() == DogBaseConstant.DISABLE_WARN){
            throw new CustomException(ResponseStatusEnum.ACCOUNT_FREEZE);
        }
        String salt = user.getSalt();
        String md5WithSalt = Md5WithSaltUtil.md5Encrypt(password,salt);

        if(!user.getPassword().equals(md5WithSalt)){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }

        //角色信息
        List<UserRole> roles = roleManager.queryRolesByUserId(user.getUserId());
        if(roles==null || roles.size()==0){
            throw new CustomException(ResponseStatusEnum.NO_PERMITION);
        }
        LoginUser loginUser = LoginUserUtil.getLoginUser(user,roles);
        //create token
        //存储信息包括userId，username,roles,userVersion
        Map<String,Object> map = new HashMap<>(8);
        map.put(WhiteJwtTokenUtil.USER_ID,user.getUserId());
        map.put(WhiteJwtTokenUtil.USERNAME,username);
        map.put(WhiteJwtTokenUtil.USER_ROLE,loginUser.getRoles());
        map.put(WhiteJwtTokenUtil.USER_VERSION,user.getUserVersion());
        String token = jwtTokenUtil.createTokenByParams(map);

        //存放登录用户的信息，方便用户获取使用,存储时间为2小时
        userCacheUtil.saveLoginUser(loginUser.getUsername(),loginUser);

        //存储用户校验使用的信息在内存中,方便快速校验
        Collection<GrantedAuthority> roleCollection = LoginUserUtil.createAuthCollection(loginUser.getRoles());
        UserDetails userDetails = new User(user.getUsername(),user.getPassword(),roleCollection);
        authCacheUtil.saveUserDetail(userDetails.getUsername(),
                userDetails);
        return token;
    }

    /**
     * 根据原有token签发新的token
     * @return
     */
    @Override
    public String updateToken() {
        String token = jwtTokenUtil.getToken();
        String newToken = jwtTokenUtil.updateToken(token);
        return newToken;
    }

    /**
     * 根据token获取用户信息
     * @return
     */
    @Override
    public LoginUser getUserByToken() {
        String username = jwtTokenUtil.getUsername();
        LoginUser user = userCacheUtil.getLoginUser(username);
        if(user ==null){
            UserBasicInfo userBasic = userManager.getUserByUsername(username);
            if(userBasic==null){
                throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            user = LoginUserUtil.getLoginUser(userBasic,null);
            //save to memory
            userCacheUtil.saveLoginUser(user.getUsername(),user);
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
        userCacheUtil.removeAllInfo(username);
        //version update
        userMapper.updateUserVersionByUsername(username);
    }
}
