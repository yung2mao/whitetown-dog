package cn.whitetown.usersecurity.service.impl;

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

        //create token
        //save ->  userId，username,roles,userVersion
        Map<String,Object> map = new HashMap<>(8);
        map.put(WhiteJwtTokenUtil.USER_ID,user.getUserId());
        map.put(WhiteJwtTokenUtil.USERNAME,username);
        map.put(WhiteJwtTokenUtil.USER_VERSION,user.getUserVersion());
        String token = jwtTokenUtil.createTokenByParams(map);

        return token;
    }

    @Override
    public String updateToken() {
        String token = jwtTokenUtil.getToken();
        String newToken = jwtTokenUtil.updateToken(token);
        return newToken;
    }

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

    @Override
    public void logout() {
        String username = jwtTokenUtil.getUsername();
        if(username==null){
            return;
        }
        userCacheUtil.removeAllUserCacheInfo(username);
        //version update
        userMapper.updateUserVersionByUsername(username);
    }
}
