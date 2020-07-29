package cn.whitetown.authea.util;


import cn.whitetown.authea.modo.AuthConstants;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;

/**
 * 用户权限涉及信息的缓存工具类
 * @author taixian
 * @date 2020/07/27
 **/
public class WhiteAuthCacheUtil implements AuthCacheUtil {

    @Autowired
    private WhiteExpireMap expireMap;

    @Override
    public UserDetails saveUserDetail(String username, UserDetails userDetails){
        Object o = expireMap.putS(AuthConstants.USER_DETAIL_PREFIX + username,
                userDetails,
                AuthConstants.USER_SAVE_TIME);
        return this.castUserDetails(o);
    }

    @Override
    public UserDetails getUserDetails(String username){
        Object o = expireMap.get(AuthConstants.USER_DETAIL_PREFIX + username);
        return this.castUserDetails(o);
    }

    @Override
    public UserDetails removeUserDetails(String username) {
        Object o = expireMap.remove(AuthConstants.USER_DETAIL_PREFIX + username);
        return this.castUserDetails(o);
    }

    @Override
    public HashSet<String> saveUserAuthors(String username, HashSet<String> authors) {
        HashSet<String> authorSet = (HashSet<String>)expireMap.putS(AuthConstants.USER_AUTHORS_PREFIX+username,
                authors,
                AuthConstants.USER_SAVE_TIME);
        return authorSet == null ? new HashSet<>() : authorSet;
    }

    @Override
    public HashSet<String> getUserAuthors(String username) {
        HashSet<String> authorSet =  (HashSet<String>) expireMap.get(AuthConstants.USER_AUTHORS_PREFIX+username);
        return authorSet == null ? new HashSet<>() : authorSet;
    }

    @Override
    public HashSet<String> removeUserAuthors(String username) {
        HashSet<String> authorSet = (HashSet<String>) expireMap.remove(AuthConstants.USER_AUTHORS_PREFIX+username);
        return authorSet == null ? new HashSet<>() : authorSet;
    }

    private UserDetails castUserDetails(Object o) {
        return !(o instanceof UserDetails) ? null : (UserDetails) o;
    }
}
