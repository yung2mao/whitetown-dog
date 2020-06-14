package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.dogbase.domain.special.WhiteExpireMap;
import cn.whitetown.dogbase.user.entity.LoginUser;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.usersecurity.mappers.UserBasicInfoMapper;
import cn.whitetown.usersecurity.service.DefaultUserDetailService;
import cn.whitetown.usersecurity.util.LoginUserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;


/**
 * @author GrainRain
 * @date 2020/06/13 15:49
 **/
@Component
public class UserDetailServiceImpl implements DefaultUserDetailService {

    @Autowired
    private WhiteExpireMap whiteExpireMap;

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
        UserDetails userDetails =  (UserDetails) whiteExpireMap.get(AuthConstant.USERDETAIL_PREFIX+username);
        if(userDetails==null){
            LambdaQueryWrapper<UserBasicInfo> condition = new LambdaQueryWrapper<>();
            condition.eq(UserBasicInfo::getUsername, username);
            UserBasicInfo userBasicInfo = userMapper.selectOne(condition);
            if(userBasicInfo==null){
                return null;
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader(AuthConstant.HEADER_STRING);
            Claims claims = jwtTokenUtil.readTokenAsMapParams(token);
            System.out.println("roles::"+claims.get("roles"));
            List<String> roles = (List<String>) claims.get("roles");

            Collection<GrantedAuthority> roleCollection = LoginUserUtil.createRoleCollection(roles);

            userDetails = new User(userBasicInfo.getUsername(),userBasicInfo.getPassword(),roleCollection);
            whiteExpireMap.putS(AuthConstant.USERDETAIL_PREFIX+username,userDetails,AuthConstant.USER_SAVE_TIME);
        }
        return userDetails;
    }
}
