package cn.whitetown.authea.util;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

/**
 * 后端权限管理-缓存工具类
 * @author taixian
 * @date 2020/07/27
 **/
public interface AuthCacheUtil {

    /**
     * 将UserDetails信息存入内存中
     * @param key
     * @param userDetails
     * @return
     */
    UserDetails saveUserDetail(String key, UserDetails userDetails);

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
    /**
     * 保存用户访问后端接口所有权限列表
     * @param key
     * @param authors
     * @return
     */
    Set<String> saveUserAuthors(String key, Set<String> authors);

    /**
     * 获取用户访问后端接口所有权限列表
     * @param key
     * @return
     */
    Set<String> getUserAuthors(String key);

    /**
     * 移除用户访问后端接口所有权限列表
     * @param key
     * @return
     */
    Set<String> removeUserAuthors(String key);

    /**
     * 清空所有用户包含的所有权限列表
     */
    void clearAllUsersAuthors();
}
