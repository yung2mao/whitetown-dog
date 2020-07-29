package cn.whitetown.authea.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 获取UserDetail默认接口
 * @author GrainRain
 * @date 2020/06/13 16:31
 **/
public interface WhiteUserDetailService extends UserDetailsService {

    /**
     * 获取UserDetail - 仅包含基本角色信息和用户密码信息
     * @param username
     * @return
     */
    UserDetails getBasicUserDetail(String username);

}
