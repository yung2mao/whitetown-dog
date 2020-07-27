package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.authcommon.entity.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户信息缓存工具类
 * @author GrainRain
 * @date 2020/06/18 09:48
 **/
public class WhiteUserCacheUtil implements UserCacheUtil {
    @Autowired
    private WhiteExpireMap expireMap;

    /**
     * 保存用户信息到内存中
     * @param key
     * @param info
     * @return
     */
    @Override
    public LoginUser saveLoginUser(String key, LoginUser info){
        return (LoginUser) expireMap.putS(key,info, AuthConstant.USER_SAVE_TIME);
    }

    /**
     * 从内存中获取user
     * @param key
     * @return
     */
    @Override
    public LoginUser getLoginUser(String key) {
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
}
