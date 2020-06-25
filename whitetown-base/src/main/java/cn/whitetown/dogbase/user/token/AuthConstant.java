package cn.whitetown.dogbase.user.token;

import sun.jvm.hotspot.jdi.SACoreAttachingConnector;

/**
 * @author GrainRain
 * @date 2020/05/31 10:14
 **/
public class AuthConstant {
    /**
     * token 有效期为7天
     */
    public static final int TOKEN_EXPIRE = 604800;
    /**
     * token还有1小时过期时重新签发
     */
    public static final int TOKEN_RESET_TIME = 3600;
    /**
     * 登录后用户信息存储时长
     */
    public static final int USER_SAVE_TIME = 7200;
    public static final String TOKEN_SECRET = "hy#@gsfds*&gvd^%((<gtyqwe4564sd:#";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_COOKIE_NAME = "token";

    /**
     * 初始密码
     */
    public static final String DEFAULT_PWD = "123456";
    /**
     * 密码验证返回的密码验证通过标记信息
     */
    public static final String PWD_TOKEN_TIME = "tokenCurrentTime";
    /**
     * 密码验证通过标记信息有效时长
     */
    public static final int PWD_TOKEN_EXPIRE_TIME = 180000;
    /**
     * 存储在内存的UserDetail key值前缀，用于区分存储用户登录使用信息的LoginUser
     */
    public static final String USERDETAIL_PREFIX = "AUTH";
    /**
     * 定义session的cookie名称
     */
    public static final String SESSION_COOKIE_NAME = "sessionId";

    /**
     * captcha保留180秒
     */
    public static final int CAPTCHA_EXPIRE_TIME = 180;

    /**
     * 注册用户初始角色
     */
    public static final String DEFAULT_ROLE = "NORMAL";
}
