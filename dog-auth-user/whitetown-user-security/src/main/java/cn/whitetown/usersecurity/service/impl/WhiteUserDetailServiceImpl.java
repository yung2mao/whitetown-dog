package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authcommon.util.defaultimpl.WhiteJwtTokenUtil;
import cn.whitetown.authea.service.WhiteUserDetailService;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.usersecurity.manager.UserManager;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import cn.whitetown.usersecurity.util.LoginUserUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;


/**
 * 获取UserDetail的实现
 * @author GrainRain
 * @date 2020/06/13 15:49
 **/
@Component
public class WhiteUserDetailServiceImpl implements WhiteUserDetailService {

    private Log log = LogFactory.getLog(WhiteUserDetailServiceImpl.class);

    @Autowired
    private AuthUserCacheUtil authCacheUtil;

    @Resource
    private UserManager userManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 根据用户名获取UserDetail用于校验登录状态
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails userDetails =  authCacheUtil.getUserDetails(username);
        if(userDetails==null){
            UserBasicInfo userBasicInfo = userManager.getUserByUsername(username);
            if(userBasicInfo==null){
                throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
            }
            if(userBasicInfo.getUserStatus() == DogBaseConstant.DISABLE_WARN){
                throw new CustomException(ResponseStatusEnum.ACCOUNT_FREEZE);
            }
            String version = jwtTokenUtil.getTokenValue(WhiteJwtTokenUtil.USER_VERSION);
            if(version==null){
                throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            Integer userVersion = Integer.valueOf(version);
            if(!userVersion.equals(userBasicInfo.getUserVersion())){
                throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            List<String> roles = (List<String>) jwtTokenUtil.getTokenValueAsObject(WhiteJwtTokenUtil.USER_ROLE);
            log.warn("当前登录的用户角色为 >>" + roles);
            Collection<GrantedAuthority> roleCollection = LoginUserUtil.createRoleCollection(roles);

            userDetails = new User(userBasicInfo.getUsername(),userBasicInfo.getPassword(),roleCollection);
        }
        authCacheUtil.saveUserDetail(username,userDetails);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return userDetails;
    }

}