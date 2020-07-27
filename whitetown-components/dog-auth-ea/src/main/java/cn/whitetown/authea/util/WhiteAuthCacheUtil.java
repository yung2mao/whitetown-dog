package cn.whitetown.authea.util;


import cn.whitetown.authea.modo.AuthConstants;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
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

    /**
     * UserDetail保存
     * @param username
     * @param userDetails
     * @return
     */
    @Override
    public UserDetails saveUserDetail(String username, UserDetails userDetails){
        return (UserDetails) expireMap.putS(AuthConstants.USER_DETAIL_PREFIX +username,
                userDetails,
                AuthConstants.USER_SAVE_TIME);
    }

    @Override
    public UserDetails getUserDetails(String username){
        return (UserDetails) expireMap.get(AuthConstants.USER_DETAIL_PREFIX +username);
    }

    @Override
    public UserDetails removeUserDetails(String username) {
        return (UserDetails)expireMap.remove(AuthConstants.USER_DETAIL_PREFIX +username);
    }


    @Override
    public HashSet<String> saveUserAuthors(String username, HashSet<String> authors) {
        return (HashSet<String>)expireMap.putS(AuthConstants.USER_AUTHORS_PREFIX+username,
                authors,
                AuthConstants.USER_SAVE_TIME);
    }

    @Override
    public HashSet<String> getUserAuthors(String username) {
        return (HashSet<String>) expireMap.get(AuthConstants.USER_AUTHORS_PREFIX+username);
    }

    @Override
    public HashSet<String> removeUserAuthors(String username) {
        return (HashSet<String>) expireMap.remove(AuthConstants.USER_AUTHORS_PREFIX+username);
    }
}
