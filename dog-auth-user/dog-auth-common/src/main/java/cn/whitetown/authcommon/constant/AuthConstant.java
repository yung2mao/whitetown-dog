package cn.whitetown.authcommon.constant;

/**
 * 用户系统相关的常量信息
 * @author GrainRain
 * @date 2020/05/31 10:14
 **/
public class AuthConstant {
    /*\*****************权限相关**************************\*/

    public static final String TOKEN_SECRET = "hy#@gsfds*&gvd^%((<gtyqwe4564sd:#";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_COOKIE_NAME = "token";
    public static final int TOKEN_EXPIRE = 432000;
    public static final int TOKEN_RESET_TIME = 3600;
    /**
     * 登录后用户信息存储时长
     */
    public static final int USER_SAVE_TIME = 900;
    /**
     * 初始密码
     */
    public static final String DEFAULT_PWD = "123456";
    /**
     * 密码验证返回的密码验证通过标记信息
     */
    public static final String PWD_TOKEN_TIME = "tokenCurrentTime";
    /**
     * 密码验证通过标记信息有效时长 - 180s
     */
    public static final int PWD_TOKEN_EXPIRE_TIME = 180000;
    /**
     * 定义客户端存储sessionId的cookie名称
     */
    public static final String SESSION_COOKIE_NAME = "sessionId";
    /**
     * captcha保留在内存保存180秒
     */
    public static final int CAPTCHA_EXPIRE_TIME = 180;

    public static final int RANDOM_SESSION_ID_LEN = 12;

    /*\*****************用户相关**************************\*/

    public static final String SUPER_MANAGE_USERNAME = "admin";

    /*\*****************角色相关**************************\*/
    /**
     * 注册用户默认角色
     */
    public static final String DEFAULT_ROLE = "NORMAL";

    /*\*****************菜单相关**************************\*/

    public static final int LOWEST_MENU_LEVEL = 100;
    public static final String MENU_CACHE_PREFIX_A = "MENU_A";
    public static final String MENU_CACHE_PREFIX_M = "MENU_M";
    public static final long MENU_SAVE_TIME = 300L;
    public static final long ROOT_MENU_ID = 1L;
    public static final int MENU_MAX_SORT = 100;

    /*\*****************部门相关**************************\*/

    public static final long ROOT_DEPT_ID = 1L;
    public static final int LOWEST_DEPT_LEVEL = 100;

    /*\*****************职位相关**************************\*/

    public static final int ONE_PERSON_LEVEL = 1;
    public static final Integer DEFAULT_DEPT_LEVEL = 1;
}
