package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.authcommon.entity.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;

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

    /**
     * UserDetail保存
     * @param username
     * @param userDetails
     * @return
     */
    @Override
    public UserDetails saveUserDetail(String username, UserDetails userDetails){
        return (UserDetails) expireMap.putS(AuthConstant.USER_DETAIL_PREFIX +username,
                userDetails,
                AuthConstant.USER_SAVE_TIME);
    }

    @Override
    public UserDetails getUserDetails(String username){
        return (UserDetails) expireMap.get(AuthConstant.USER_DETAIL_PREFIX +username);
    }

    @Override
    public UserDetails removeUserDetails(String username) {
        return (UserDetails)expireMap.remove(AuthConstant.USER_DETAIL_PREFIX +username);
    }

    @Override
    public HashSet<String> saveUserAuthors(String username,HashSet<String> authors) {
        return (HashSet<String>)expireMap.putS(AuthConstant.USER_AUTHORS_PREFIX+username,
                authors,
                AuthConstant.MENU_SAVE_TIME);
    }

    @Override
    public HashSet<String> getUserAuthors(String username) {
        return (HashSet<String>) expireMap.get(AuthConstant.USER_AUTHORS_PREFIX+username);
    }

    @Override
    public HashSet<String> removeUserAuthors(String username) {
        return (HashSet<String>) expireMap.remove(AuthConstant.USER_AUTHORS_PREFIX+username);
    }

    @Override
    public void removeAllUserInfo(String username) {
        this.removeLoginUser(username);
        this.removeUserDetails(username);
        this.removeUserAuthors(username);
    }
}
