package cn.whitetown.usersecurity.service;

import cn.whitetown.dogbase.user.entity.LoginUser;

/**
 * @author GrainRain
 * @date 2020/06/13 10:28
 **/
public interface DogUserService {
    /**
     * 验证码校验
     * @param captcha
     * @param clientIP
     */
    void checkCaptcha(String captcha, String clientIP);

    /**
     * 执行登录逻辑，校验用户名和密码并生成token
     * @param username
     * @param password
     * @return
     */
    String checkUserNameAndPassword(String username, String password);

    /**
     * 根据用户的token信息获取用户信息
     * @param token
     * @return
     */
    LoginUser getUserByToken(String token);

    /**
     * 退出登录
     * @param token
     */
    void logout(String token);
}
