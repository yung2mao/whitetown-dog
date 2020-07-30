package cn.whitetown.authea.service;

import cn.whitetown.authea.modo.WhiteSecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 获取UserDetail默认接口
 * @author GrainRain
 * @date 2020/06/13 16:31
 **/
public interface WhiteUserDetailService extends UserDetailsService {

    /**
     * 获取基础UserDetail,不含角色权限信息
     * @param username
     * @return
     */
    UserDetails loadBasicUserDetails(String username);

}
