package cn.whitetown.usersingle.service;

import cn.whitetown.authcommon.entity.dto.LoginUser;

/**
 * @author GrainRain
 * @date 2020/05/29 22:21
 **/
public interface LoginService {
    /**
     * 校验用户名和密码以实现登录逻辑
     * @param username
     * @param password
     * @return
     */
    String checkUsernameAndPassword(String username, String password);

    /**
     * 检查登录状态
     * @param token
     * @return
     */
    String checkLogin(String token);

    LoginUser getUserInfo(String token);

}
