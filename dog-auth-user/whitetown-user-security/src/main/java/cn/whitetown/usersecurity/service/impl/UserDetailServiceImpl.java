package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.dogbase.domain.special.WhiteExpireMap;
import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.exception.CustomException;
import cn.whitetown.dogbase.user.entity.LoginUser;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.dogbase.user.util.UserCacheUtil;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.service.DefaultUserDetailService;
import cn.whitetown.usersecurity.util.LoginUserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;


/**
 * 获取UserDetail的实现
 * @author GrainRain
 * @date 2020/06/13 15:49
 **/
@Component
public class UserDetailServiceImpl implements DefaultUserDetailService {

    private Log log = LogFactory.getLog(UserDetailServiceImpl.class);

    @Autowired
    private UserCacheUtil userCacheUtil;

    @Resource
    private UserBasicInfoMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 根据用户名获取UserDetail用于校验登录状态
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails userDetails =  userCacheUtil.getUserDetails(AuthConstant.USERDETAIL_PREFIX+username);
        if(userDetails==null){
            LambdaQueryWrapper<UserBasicInfo> condition = new LambdaQueryWrapper<>();
            condition.eq(UserBasicInfo::getUsername, username);
            UserBasicInfo userBasicInfo = userMapper.selectOne(condition);
            if(userBasicInfo==null){
                throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
            }
            String version = jwtTokenUtil.getTokenValue(JwtTokenUtil.USER_VERSION);
            if(version==null){
                throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            Integer userVersion = Integer.valueOf(version);
            if(!userVersion.equals(userBasicInfo.getUserVersion())){
                throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            List<String> roles = (List<String>) jwtTokenUtil.getTokenValueAsObject(JwtTokenUtil.USER_ROLE);
            log.warn("当前登录的用户角色为 >>" + roles);
            Collection<GrantedAuthority> roleCollection = LoginUserUtil.createRoleCollection(roles);

            userDetails = new User(userBasicInfo.getUsername(),userBasicInfo.getPassword(),roleCollection);
            userCacheUtil.saveUserDetail(AuthConstant.USERDETAIL_PREFIX+username,userDetails);
        }
        return userDetails;
    }
}