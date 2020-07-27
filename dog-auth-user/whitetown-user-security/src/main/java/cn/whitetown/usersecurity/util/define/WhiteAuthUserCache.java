package cn.whitetown.usersecurity.util.define;

import cn.whitetown.authcommon.entity.dto.LoginUser;
import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authea.util.WhiteAuthCacheUtil;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;

/**
 * 用户模块缓存工具
 * @author taixian
 * @date 2020/07/27
 **/
public class WhiteAuthUserCache extends WhiteAuthCacheUtil implements AuthUserCacheUtil {

    private UserCacheUtil userCacheUtil;

    public WhiteAuthUserCache(UserCacheUtil userCacheUtil) {
        if(userCacheUtil==null) {
            throw new NullPointerException("user cache is null");
        }
        this.userCacheUtil = userCacheUtil;
    }


    @Override
    public void removeAllInfo(String key) {
        userCacheUtil.removeLoginUser(key);
        this.removeUserDetails(key);
        this.removeUserAuthors(key);
    }

    @Override
    public LoginUser saveLoginUser(String key, LoginUser info) {
        return userCacheUtil.saveLoginUser(key,info);
    }

    @Override
    public LoginUser getLoginUser(String key) {
        return userCacheUtil.getLoginUser(key);
    }

    @Override
    public LoginUser removeLoginUser(String key) {
        return userCacheUtil.removeLoginUser(key);
    }
}
