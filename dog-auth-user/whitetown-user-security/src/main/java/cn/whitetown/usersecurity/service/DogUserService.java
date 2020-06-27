package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.vo.LoginUser;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务
 * @author GrainRain
 * @date 2020/06/13 10:28
 **/
public interface DogUserService extends IService<UserBasicInfo> {
    /**
     * 验证码校验
     * @param captcha
     * @param clientIp
     */
    void checkCaptcha(String captcha, String clientIp);

    /**
     * 执行登录逻辑，校验用户名和密码并生成token
     * @param username
     * @param password
     * @return
     */
    String checkUserNameAndPassword(String username, String password);

    /**
     * 签发一个新的token
     * @return
     */
    String updateToken();

    /**
     * 根据用户的token信息获取用户信息
     * @return
     */
    LoginUser getUserByToken();

    /**
     * 退出登录
     */
    void logout();
}
