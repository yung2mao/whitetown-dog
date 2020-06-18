package cn.whitetown.usersingle.service;

import cn.whitetown.dogbase.domain.special.WhiteExpireMap;
import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.exception.CustomException;
import cn.whitetown.dogbase.user.entity.LoginUser;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import cn.whitetown.dogbase.user.entity.UserRole;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.dogbase.util.DataCheckUtil;
import cn.whitetown.dogbase.util.FormatUtil;
import cn.whitetown.dogbase.util.secret.MD5WithSaltUtil;
import cn.whitetown.usersingle.mappers.UserBasicInfoMapper;
import cn.whitetown.usersingle.util.LoginUserUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private UserBasicInfoMapper userMapper;

    private Log log = LogFactory.getLog(SessionLoginService.class);

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
        String md5WithSalt = MD5WithSaltUtil.md5Encrypt(password,salt);

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
        //存放登录用户的信息，方便用户获取使用
        whiteExpireMap.put(loginUser.getUsername(),loginUser,7200000);
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
            if(DataCheckUtil.checkTextNullBool(username)){
                throw new CustomException(ResponseStatusEnum.TOKEN_ERROR);
            }

            List<String> roles = (List<String>)userMap.get("roles");
            //校验token是否即将过期，如果是则签发一个新的token
            Date expiration = userMap.getExpiration();
            log.warn("\n当前用户角色为："+roles+"<  > token过期时间为："+expiration);

            long expireTime = FormatUtil.timeAsLong(expiration);
            if(expireTime - System.currentTimeMillis() < 12000*AuthConstant.TOKEN_RESET_TIME){
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
    public LoginUser getUserInfo(String token){
        String username = getUsernameByToken(token);
        LoginUser user = (LoginUser)whiteExpireMap.get(username);
        if(user ==null){
            UserBasicInfo userBasic = userMapper.selectUserByUsername(username);
            if(userBasic==null){
                throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
            }
            user = LoginUserUtil.getLoginUser(userBasic,null);
        }
        user.setRoles(null);
        return user;
    }

    /**
     * 通过token获取用户名信息
     * @param token
     * @return
     */
    String getUsernameByToken(String token) {
        Claims userMap = jwtTokenUtil.readTokenAsMapParams(token);
        String username = userMap.get("username").toString();
        if (DataCheckUtil.checkTextNullBool(username)) {
            throw new CustomException(ResponseStatusEnum.TOKEN_ERROR);
        }
        return username;
    }

}
