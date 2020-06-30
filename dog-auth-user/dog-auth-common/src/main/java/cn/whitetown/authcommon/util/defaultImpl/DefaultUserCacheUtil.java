package cn.whitetown.authcommon.util.defaultImpl;

import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.authcommon.entity.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;

/**
 * 用户信息缓存工具类
 * @author GrainRain
 * @date 2020/06/18 09:48
 **/
public class DefaultUserCacheUtil implements UserCacheUtil {
    @Autowired
    private WhiteExpireMap expireMap;

    /**
     * 保存用户信息到内存中
     * @param key
     * @param info
     * @return
     */
    @Override
    public LoginUser saveUserBasicInfo(String key, LoginUser info){
        return (LoginUser) expireMap.putS(key,info, AuthConstant.USER_SAVE_TIME);
    }

    /**
     * 从内存中获取user
     * @param key
     * @return
     */
    @Override
    public LoginUser getUserBasicInfo(String key) {
        return (LoginUser) expireMap.get(key);
    }

    /**
     * 从内存中移除登录用户的信息
     * @param key
     * @return
     */
    @Override
    public LoginUser removeLoginUser(String key){
        return (LoginUser) expireMap.remove(key);
    }

    /**
     * UserDetail保存
     * @param key
     * @param userDetails
     * @return
     */
    @Override
    public UserDetails saveUserDetail(String key, UserDetails userDetails){
        return (UserDetails) expireMap.putS(key,
                userDetails,
                AuthConstant.USER_SAVE_TIME);
    }

    @Override
    public UserDetails getUserDetails(String key){
        return (UserDetails) expireMap.get(key);
    }

    @Override
    public UserDetails removeUserDetails(String key) {
        return (UserDetails)expireMap.remove(key);
    }

    @Override
    public void removeUserInfo(String... keys) {
        if(keys == null || keys.length==0){
            return;
        }
        Arrays.stream(keys).forEach(key->expireMap.remove(key));
    }
}
