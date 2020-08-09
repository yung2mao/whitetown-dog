package cn.whitetown.authea.util;

import cn.whitetown.authea.modo.AuthConstants;
import cn.whitetown.dogbase.wache.WhiteExpireMap;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户权限涉及信息的缓存工具类
 * @author taixian
 * @date 2020/07/27
 **/
public class WhiteAuthCacheUtil implements AuthCacheUtil {

    private Logger logger = LogConstants.sysLogger;

    @Autowired
    private WhiteExpireMap expireMap;

    private Set<String> userSet;

    {
        userSet = ConcurrentHashMap.newKeySet();
    }

    @Override
    public UserDetails saveUserDetail(String username, UserDetails userDetails){
        Object o = expireMap.putS(AuthConstants.USER_DETAIL_PREFIX + username,
                userDetails,
                AuthConstants.USER_SAVE_TIME);
        userSet.add(AuthConstants.USER_DETAIL_PREFIX + username);
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
    public Set<String> saveUserAuthors(String username, Set<String> authors) {
        HashSet<String> authorSet = null;
        try {
           authorSet = (HashSet<String>) expireMap.putS(AuthConstants.USER_AUTHORS_PREFIX + username,
                    authors,
                    AuthConstants.USER_SAVE_TIME);
           userSet.add(username);
        }catch (Exception e) {
            return new HashSet<>();
        }
        return authorSet == null ? new HashSet<>() : authorSet;
    }

    @Override
    public Set<String> getUserAuthors(String username) {
        HashSet<String> authorSet = null;
        try {
            authorSet = (HashSet<String>) expireMap.get(AuthConstants.USER_AUTHORS_PREFIX + username);
        }catch (Exception e) {
            return new HashSet<>();
        }
        return authorSet == null ? new HashSet<>() : authorSet;
    }

    @Override
    public Set<String> removeUserAuthors(String username) {
        HashSet<String> authorSet = null;
        try {
            authorSet = (HashSet<String>) expireMap.remove(AuthConstants.USER_AUTHORS_PREFIX + username);
        }catch (Exception e) {
            return new HashSet<>();
        }
        return authorSet == null ? new HashSet<>() : authorSet;
    }

    @Override
    public void clearAllUsersAuthors() {
        for(String username : userSet) {
            this.removeUserAuthors(username);
            this.removeUserDetails(username);
        }
        userSet.clear();
        logger.info("the cache authors is clear");
    }

    private UserDetails castUserDetails(Object o) {
        return !(o instanceof UserDetails) ? null : (UserDetails) o;
    }
}
