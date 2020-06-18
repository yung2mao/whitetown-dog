package cn.whitetown.dogbase.user.util;

import cn.whitetown.dogbase.user.entity.LoginUser;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 与用户相关信息内存的操作
 * @author GrainRain
 * @date 2020/06/18 09:51
 **/
public interface UserCacheUtil {
    /**
     * 保存用户信息到内存中
     * @param key
     * @param info
     * @return
     */
    LoginUser saveUserBasicInfo(String key, LoginUser info);

    /**
     * 从内存中获取UserBasic
     * @param key
     * @return
     */
    LoginUser getUserBasicInfo(String key);

    /**
     * 移除内存中存储的登录信息
     * @param key
     * @return
     */
    LoginUser removeLoginUser(String key);

    /**
     * 将UserDetails信息存入内存中
     * @param key
     * @param userDetails
     * @return
     */
    UserDetails saveUserDetail(String key,UserDetails userDetails);

    /**
     * 将UserDetails从内存中取出
     * @param key
     * @return
     */
    UserDetails getUserDetails(String key);

    /**
     * 从内存移除UserDetails信息
     * 基于用户信息版本号进行控制
     * @param key
     * @return
     */
    UserDetails removeUserDetails(String key);
}
