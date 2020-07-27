package cn.whitetown.authea.modo;

/**
 * 权限相关常量信息
 * @author taixian
 * @date 2020/07/27
 **/
public class AuthConstants {
    /**
     * 存储在内存的UserDetail key值前缀，用于区分存储用户登录使用信息的LoginUser
     */
    public static final String USER_DETAIL_PREFIX = "USERDETAIL";
    /**
     * 存储在内存的用户所有权限列表前缀
     */
    public static final String USER_AUTHORS_PREFIX = "AUTHORS";
    /**
     * 登录后用户信息存储时长
     */
    public static final int USER_SAVE_TIME = 1800;
}
