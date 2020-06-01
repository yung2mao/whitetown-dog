package cn.whitetown.usersingle.service;

import cn.whitetown.dogbase.domain.special.WhiteExpireMap;
import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.exception.CustomException;
import cn.whitetown.dogbase.user.entity.LoginUser;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import cn.whitetown.dogbase.user.entity.UserRole;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.dogbase.util.FormatUtil;
import cn.whitetown.dogbase.util.secret.SaltUtil;
import cn.whitetown.usersingle.mappers.UserBasicInfoMapper;
import cn.whitetown.usersingle.util.LoginUserUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 基于类似session形式登录实现
 * @author GrainRain
 * @date 2020/05/29 22:21
 **/
@Service
public class SessionLoginService implements LoginService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WhiteExpireMap whiteExpireMap;

    @Autowired
    private UserBasicInfoMapper userMapper;

    /**
     * 用户名和密码校验
     * @param username
     * @param password
     * @return
     */
    @Override
    public String checkUsernameAndPassword(String username, String password) {
        UserBasicInfo user = userMapper.selectUserByUsername(username);
        if(user==null){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        String salt = user.getSalt();
        String md5WithSalt = SaltUtil.md5Encrypt(password,salt);

        if(!user.getPassword().equals(md5WithSalt)){
            throw new CustomException(ResponseStatusEnum.AUTH_REQUEST_ERROR);
        }
        //角色信息
        String roleId = user.getRoleId();
        List<Long> roleIds = null;
        if(roleId != null){
            roleIds = Arrays.stream(roleId.split(",")).
                    map(id->Long.parseLong(id)).collect(Collectors.toList());
        }
        if(roleIds==null || roleIds.size()==0){
            throw new CustomException(ResponseStatusEnum.NO_PERMITION);
        }
        List<UserRole> roles = userMapper.selectUserRole(roleIds);
        if(roles==null || roles.size()==0){
            throw new CustomException(ResponseStatusEnum.NO_PERMITION);
        }
        LoginUser loginUser = LoginUserUtil.getLoginUser(user,roles);
        //create token
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        map.put("roles",loginUser.getRoles());
        String token = jwtTokenUtil.createTokenByParams(map);
        //存放用户信息，方便用户获取使用
        whiteExpireMap.put(user.getUsername(),user,7200000);
        return token;
    }

    /**
     * 登录状态校验
     * @param token
     * @return
     */
    @Override
    public String checkLogin(String token) {
        try {
            Claims userMap = jwtTokenUtil.readTokenAsMapParams(token);
            String username = userMap.get("username").toString();
            if(FormatUtil.checkTextNullBool(username)){
                throw new CustomException(ResponseStatusEnum.TOKEN_ERROR);
            }

            List<String> roles = (List<String>)userMap.get("roles");
            //校验token是否即将过期，如果是则签发一个新的token
            Date expiration = userMap.getExpiration();
            //TODO: reset as log
            System.out.println("当前用户角色为："+roles+"<  > token过期时间为："+expiration);
            long expireTime = FormatUtil.timeAsLong(expiration);
            if(expireTime - System.currentTimeMillis() < 1000*AuthConstant.TOKEN_RESET_TIME){
                //create new token
                Map<String,Object> map = new HashMap<>();
                map.put("username",username);
                map.put("roles",roles);
                String newToken = jwtTokenUtil.createTokenByParams(map);
                return newToken;
            }
        }catch (ExpiredJwtException e){
            throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
        } catch (Exception e){
            throw new CustomException(ResponseStatusEnum.TOKEN_ERROR);
        }
        return null;
    }

    /**
     * 获取用户基本信息
     * @param token
     * @return
     */
    @Override
    public UserBasicInfo getUserInfo(String token){
        String username = getUsernameByToken(token);
        UserBasicInfo user = (UserBasicInfo)whiteExpireMap.get(username);
        if(user ==null){
            user = userMapper.selectUserByUsername(username);
        }
        if(user==null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
        }
        user.setPassword("");
        return user;
    }

    String getUsernameByToken(String token) {
        Claims userMap = jwtTokenUtil.readTokenAsMapParams(token);
        String username = userMap.get("username").toString();
        if (FormatUtil.checkTextNullBool(username)) {
            throw new CustomException(ResponseStatusEnum.TOKEN_ERROR);
        }
        return username;
    }

}
