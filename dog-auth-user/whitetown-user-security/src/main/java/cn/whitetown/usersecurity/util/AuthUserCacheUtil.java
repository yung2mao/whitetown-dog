package cn.whitetown.usersecurity.util;

import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authea.util.AuthCacheUtil;

/**
 * 用户模块缓存工具
 * @author taixian
 * @date 2020/07/27
 **/
public interface AuthUserCacheUtil extends UserCacheUtil, AuthCacheUtil {
    /**
     * 移除缓存中所有用户权限相关信息
     * @param key
     */
    void removeAllInfo(String key);
}
