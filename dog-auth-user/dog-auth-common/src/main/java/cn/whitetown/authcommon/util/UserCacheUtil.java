package cn.whitetown.authcommon.util;

import cn.whitetown.authcommon.entity.dto.LoginUser;

/**
 * 与用户相关信息内存的操作
 * @author GrainRain
 * @date 2020/06/18 09:51
 **/
public interface UserCacheUtil {
    /**
     * 保存用户登录信息到内存中
     * @param key
     * @param info
     * @return
     */
    LoginUser saveLoginUser(String key, LoginUser info);

    /**
     * 从内存中获取LoginUser
     * @param key
     * @return
     */
    LoginUser getLoginUser(String key);

    /**
     * 移除内存中存储的登录信息
     * @param key
     * @return
     */
    LoginUser removeLoginUser(String key);

    /**
     * 验证码信息缓存
     * @param sessionId
     * @param captcha
     * @return
     */
    String saveCaptcha(String sessionId,String captcha);

    /**
     * 从缓存获取验证码获取
     * @param sessionId
     * @return
     */
    String getCaptcha(String sessionId);
}
