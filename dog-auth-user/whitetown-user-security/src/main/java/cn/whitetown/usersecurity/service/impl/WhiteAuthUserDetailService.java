package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.dto.LoginUser;
import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authcommon.util.defaultimpl.WhiteJwtTokenUtil;
import cn.whitetown.authea.manager.SpringSecurityConfigureManager;
import cn.whitetown.authea.manager.UserDetailManager;
import cn.whitetown.authea.modo.AuthUser;
import cn.whitetown.authea.modo.WhiteSecurityUser;
import cn.whitetown.authea.service.WhiteUserDetailService;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import cn.whitetown.usersecurity.util.LoginUserUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * @author taixian
 * @date 2020/07/31
 **/
@Component
public class WhiteAuthUserDetailService implements WhiteUserDetailService {

    private Log log = LogFactory.getLog(WhiteUserDetailServiceImpl.class);

    @Autowired
    private AuthUserCacheUtil authCacheUtil;

    @Autowired
    private UserDetailManager userDetailManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) {
        try{
            return this.loadBasicUserDetails(username);
        }catch (Exception e){
            log.warn("load userDetail error, the reason is "+e.getMessage());
            throw new CustomException(ResponseStatusEnum.TOKEN_ERROR);
        }
    }

    @Override
    public UserDetails loadBasicUserDetails(String username) {
        UserDetails userDetails = authCacheUtil.getUserDetails(username);
        if(userDetails != null) {
            return userDetails;
        }
        AuthUser authUser = userDetailManager.createAuthUser(username);
        if(authUser == null) {
            throw new CustomException(ResponseStatusEnum.TOKEN_ERROR);
        }
        String versionString = jwtTokenUtil.getTokenValue(WhiteJwtTokenUtil.USER_VERSION);
        Integer version = versionString == null ? -1 : Integer.valueOf(versionString);
        if(version==null || !version.equals(authUser.getUserVersion())){
            throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
        }
        WhiteSecurityUser whiteSecurityUser = new WhiteSecurityUser(authUser.getUsername(), authUser.getPassword());
        whiteSecurityUser.setUserId(authUser.getUserId());

        Set<String> authorsSet = userDetailManager.createAuthorsSet(authUser);
        Collection<GrantedAuthority> authCollection = LoginUserUtil.createAuthCollection(authorsSet);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(whiteSecurityUser.getUsername(),null, authCollection);
        whiteSecurityUser.setAuthentication(authentication);

        userDetails = whiteSecurityUser;
        authCacheUtil.saveUserDetail(username,userDetails);
        return userDetails;
    }
}
