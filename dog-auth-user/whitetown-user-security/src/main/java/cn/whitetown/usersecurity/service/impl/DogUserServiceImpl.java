package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.dogbase.domain.special.WhiteExpireMap;
import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.exception.CustomException;
import cn.whitetown.dogbase.user.captcha.CaptchaDataDeal;
import cn.whitetown.dogbase.user.entity.LoginUser;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import cn.whitetown.dogbase.user.entity.UserRole;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.dogbase.util.DataCheckUtil;
import cn.whitetown.dogbase.util.secret.SaltUtil;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.service.DogUserService;
import cn.whitetown.usersecurity.util.LoginUserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class DogUserServiceImpl implements DogUserService {

    @Autowired
    private CaptchaDataDeal captchaDataDeal;

    @Resource
    private UserBasicInfoMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WhiteExpireMap whiteExpireMap;

    /**
     * 验证码校验
     * @param captcha
     * @param clientIP
     */
    @Override
    public void checkCaptcha(String captcha, String clientIP) {
        if(DataCheckUtil.checkTextNullBool(captcha)){
            throw new CustomException(ResponseStatusEnum.ERROR_PARAMS);
        }
        String saveIP = captchaDataDeal.getCaptcha(captcha.toLowerCase());
        if(DataCheckUtil.checkTextNullBool(saveIP)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
        //校验两次IP是否相同，由此判断是否同一用户
        if(!saveIP.equalsIgnoreCase(clientIP)){
            throw new CustomException(ResponseStatusEnum.AUTH_CAPTCHA_ERROR);
        }
    }

    /**
     * 用户名密码校验
     * @param username
     * @param password
     * @return
     */
    @Override
    public String checkUserNameAndPassword(String username, String password) {
        if(DataCheckUtil.checkTextNullBool(username) || DataCheckUtil.checkTextNullBool(password)){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        LambdaQueryWrapper<UserBasicInfo> condition = new LambdaQueryWrapper<>();
        condition.eq(UserBasicInfo::getUsername,username);
        UserBasicInfo user = userMapper.selectOne(condition);
        if(user==null){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        String salt = user.getSalt();
        String md5WithSalt = SaltUtil.md5Encrypt(password,salt);

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
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("roles",loginUser.getRoles());
        String token = jwtTokenUtil.createTokenByParams(map);

        //存放登录用户的信息，方便用户获取使用,存储2小时
        whiteExpireMap.putS(loginUser.getUsername(),loginUser, AuthConstant.USER_SAVE_TIME);

        //存储用户校验使用的信息在内存中，作为登录的凭证
        Collection<GrantedAuthority> roleCollection = LoginUserUtil.createRoleCollection(loginUser.getRoles());
        UserDetails userDetails = new User(user.getUsername(),user.getPassword(),roleCollection);
        whiteExpireMap.putS(AuthConstant.USERDETAIL_PREFIX+userDetails.getUsername(),
                userDetails,
                AuthConstant.USER_SAVE_TIME);
        return token;
    }

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    @Override
    public LoginUser getUserByToken(String token) {
        String username = jwtTokenUtil.getUsername(token);
        LoginUser user = (LoginUser)whiteExpireMap.get(username);
        if(user ==null){
            LambdaQueryWrapper<UserBasicInfo> condition = new LambdaQueryWrapper<>();
            condition.eq(UserBasicInfo::getUsername,username);
            UserBasicInfo userBasic = userMapper.selectOne(condition);
            if(userBasic==null){
                throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            user = LoginUserUtil.getLoginUser(userBasic,null);
            //save to memory
            whiteExpireMap.putS(user.getUsername(),user, AuthConstant.USER_SAVE_TIME);
        }
        return user;
    }

    /**
     * 退出登录
     * @param token
     */
    @Override
    public void logout(String token) {
        String username = jwtTokenUtil.getUsername(token);
        whiteExpireMap.remove(username);
        whiteExpireMap.remove(AuthConstant.USERDETAIL_PREFIX+username);
    }
}
