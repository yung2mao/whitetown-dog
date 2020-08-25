package cn.whitetown.usersecurity.util.define;

import cn.whitetown.authcommon.entity.dto.LoginUser;
import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authea.util.WhiteAuthCacheUtil;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.logging.Logger;

/**
 * 用户模块缓存工具
 * @author taixian
 * @date 2020/07/27
 **/
public class WhiteAuthUserCache extends WhiteAuthCacheUtil implements AuthUserCacheUtil {

    private Log log = LogFactory.getLog(WhiteAuthUserCache.class);

    private UserCacheUtil userCacheUtil;

    public WhiteAuthUserCache(UserCacheUtil userCacheUtil) {
        if(userCacheUtil==null) {
            throw new NullPointerException("user cache is null");
        }
        this.userCacheUtil = userCacheUtil;
    }


    @Override
    public void removeAllUserCacheInfo(String key) {
        userCacheUtil.removeLoginUser(key);
        this.removeUserDetails(key);
        this.removeUserAuthors(key);
        log.info("the user [" + key + "], cache info is clear");
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

    @Override
    public String saveCaptcha(String sessionId, String captcha) {
        return userCacheUtil.saveCaptcha(sessionId,captcha);
    }

    @Override
    public String getCaptcha(String sessionId) {
        return userCacheUtil.getCaptcha(sessionId);
    }
}
