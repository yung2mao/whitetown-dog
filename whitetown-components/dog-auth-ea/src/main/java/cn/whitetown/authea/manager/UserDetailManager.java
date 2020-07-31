package cn.whitetown.authea.manager;

import cn.whitetown.authea.modo.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

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

    /**
     * 基于权限用户实体创建用户权限set
     * @param authUser
     * @return
     */
    Set<String> createAuthorsSet(AuthUser authUser);

    /**
     * 构建UserDetails
     * @return
     */
    UserDetails createUserDetails();
}
