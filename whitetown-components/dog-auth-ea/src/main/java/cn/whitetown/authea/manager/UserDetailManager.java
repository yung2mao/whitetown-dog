package cn.whitetown.authea.manager;

import cn.whitetown.authea.modo.AuthUser;

import java.util.List;

/**
 * Security UserDetail管理
 * @author taixian
 * @date 2020/07/30
 **/
public interface UserDetailManager {

    /**
     * 获取用户基本信息
     * @param username
     * @return
     */
    AuthUser createAuthUser(String username);

    /**
     * 获取角色信息
     * @param username
     * @return
     */
    List<String> getRoles(String username);

    /**
     * 获取权限信息
     * @param username
     * @return
     */
    List<String> getAuthors(String username);
}
