package cn.whitetown.dogbase.user.token;

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
    public static final String TOKEN_SECRET = "hy#@gsfds*&gvd^%((<gtyqwe4564sd:#";
    public static final String TOKEN_PREFIX = "";
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_COOKIE_NAME = "token";
    public static final String SESSION_COOKIE_NAME = "sessionId";
}
