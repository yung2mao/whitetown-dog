package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authcommon.util.defaultimpl.WhiteJwtTokenUtil;
import cn.whitetown.authea.manager.SpringSecurityConfigureManager;
import cn.whitetown.authea.manager.UserDetailManager;
import cn.whitetown.authea.modo.AuthUser;
import cn.whitetown.authea.modo.WhiteSecurityUser;
import cn.whitetown.authea.service.WhiteUserDetailService;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


/**
 * 获取UserDetail的实现
 * @author GrainRain
 * @date 2020/06/13 15:49
 **/
//@Component
public class WhiteUserDetailServiceImpl implements WhiteUserDetailService {

    @Autowired
    private AuthUserCacheUtil authCacheUtil;

    @Autowired
    private UserDetailManager userDetailManager;

    @Autowired
    private SpringSecurityConfigureManager securityConfigureManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails userDetails = this.loadBasicUserDetails(jwtTokenUtil.getToken());
        if(!(userDetails instanceof WhiteSecurityUser)) {
            return userDetails;
        }
        WhiteSecurityUser whiteSecurityUser = (WhiteSecurityUser) userDetails;
        return this.pathWithAuthorHandle(whiteSecurityUser);
    }

    @Override
    public UserDetails loadBasicUserDetails(String token) {
        Claims claims = jwtTokenUtil.readTokenAsMapParams(token);
        String username = claims.get(WhiteJwtTokenUtil.USERNAME,String.class);
        UserDetails userDetails =  authCacheUtil.getUserDetails(username);
        if(userDetails == null){
            AuthUser authUser = userDetailManager.createAuthUser(username);
            if(authUser == null) {
                throw new WhResException(ResponseStatusEnum.TOKEN_ERROR);
            }
            Integer version = claims.get(WhiteJwtTokenUtil.USER_VERSION, Integer.class);
            if(version==null || !version.equals(authUser.getUserVersion())){
                throw new WhResException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            userDetails = new WhiteSecurityUser(authUser.getUsername(),authUser.getPassword());
            Set<String> authorsSet = userDetailManager.createAuthorsSet(authUser);
            authCacheUtil.saveUserAuthors(username,authorsSet);
        }
        authCacheUtil.saveUserDetail(username,userDetails);
        return userDetails;
    }

    private WhiteSecurityUser pathWithAuthorHandle(WhiteSecurityUser userDetails) {
        String uri = WebUtil.getUri();
        String[] authors = securityConfigureManager.getAuthorsByPath(uri);
        System.out.println(uri + "所需访问权限为:" + authors);
        if(authors == null || authors.length == 0){
            return userDetails;
        }

        String username = userDetails.getUsername();
        Set<String> userAuthors = authCacheUtil.getUserAuthors(username);
        if(userAuthors == null || userAuthors.size() == 0) {
            AuthUser authUser = userDetailManager.createAuthUser(username);
            userAuthors = userDetailManager.createAuthorsSet(authUser);
        }
        authCacheUtil.saveUserAuthors(username,userAuthors);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String author : authors) {
            if(userAuthors.contains(author)) {
                authorities.add(new SimpleGrantedAuthority(author));
            }
        }
        userDetails.setAuthorities(authorities);
        return userDetails;
    }
}
