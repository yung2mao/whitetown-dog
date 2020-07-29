package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authcommon.util.defaultimpl.WhiteJwtTokenUtil;
import cn.whitetown.authea.manager.SpringSecurityConfigureManager;
import cn.whitetown.authea.modo.WhiteSecurityUser;
import cn.whitetown.authea.service.WhiteUserDetailService;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.common.util.WebUtil;
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
import java.util.*;


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

    @Autowired
    private SpringSecurityConfigureManager securityConfigureManager;

    @Resource
    private UserManager userManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails basicUserDetail = this.getBasicUserDetail(username);

        String uri = WebUtil.getUri();
        String[] authors = securityConfigureManager.getAuthorsByPath(uri);
        if(authors == null || authors.length == 0){
            return basicUserDetail;
        }
        HashSet<String> userAuthors = authCacheUtil.getUserAuthors(username);
        if(userAuthors.size() == 0) {

        }
        if(userAuthors.containsAll(Arrays.asList(authors))){
            Collection<GrantedAuthority> authCollection = LoginUserUtil.createAuthCollection(userAuthors);
            if(basicUserDetail instanceof WhiteSecurityUser) {
                ((WhiteSecurityUser)basicUserDetail).setAuthorities(authCollection);
                return basicUserDetail;
            }else {
                return new User(basicUserDetail.getUsername(),basicUserDetail.getPassword(),authCollection);
            }
        }

        return basicUserDetail;
    }

    @Override
    public UserDetails getBasicUserDetail(String username) {
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
//            List<String> roles = (List<String>) jwtTokenUtil.getTokenValueAsObject(WhiteJwtTokenUtil.USER_ROLE);
//            log.warn("当前登录的用户角色为 >>" + roles);
//            Collection<GrantedAuthority> authCollection = LoginUserUtil.createAuthCollection(roles);

            userDetails = new WhiteSecurityUser(userBasicInfo.getUsername(),userBasicInfo.getPassword(),new ArrayList<>());
        }
        authCacheUtil.saveUserDetail(username,userDetails);
        return userDetails;
    }

}
