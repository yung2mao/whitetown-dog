package cn.whitetown.dogbase.user.token;

import cn.whitetown.dogbase.util.WebUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author GrainRain
 * @date 2020/05/30 09:23
 **/
public class JwtTokenUtil {
    /**
     * 用户名键值
     */
    public static final String USERNAME = "username";
    /**
     * 用户id键值
     */
    public static final String USER_ID = "userId";
    /**
     * 用户版本号键值 - 用于控制用户token有效性
     */
    public static final String USER_VERSION = "userVersion";
    /**
     * 用户角色的键值
     */
    public static final String USER_ROLE = "role";
    /**
     * 用户ip地址
     */
    public static final String USER_IP = "userIp";
    /**
     * 过期时间
     */
    private Long EXPIRATION_TIME;
    /**
     * 私钥
     */
    private String SECRET;
    /**
     * token前缀
     */
    private final String TOKEN_PREFIX = AuthConstant.TOKEN_PREFIX;
    /**
     * 请求头名称
     */
    private final String HEADER_STRING = AuthConstant.HEADER_STRING;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");

    public JwtTokenUtil(String secret, long expire) {
        this.EXPIRATION_TIME = expire;
        this.SECRET = secret;
        System.out.println("正在初始化Jwthelper，expire="+expire);
    }
    /**
     * 生成Token
     * @param claims 需要保存在token的参数，如用户名username，用户ID
     * @return
     */
    public String createTokenByParams(Map<String, Object> claims) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.SECOND, EXPIRATION_TIME.intValue());
        Date d = c.getTime();
        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(d)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + jwtToken;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public Claims readTokenAsMapParams(String token) {
        if (token == null) {
            return null;
        }
        Claims body = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX,""))
                .getBody();
        return body;
    }

    /**
     * token更新
     * @param token
     * @return
     */
    public String updateToken(String token){
        Map<String, Object> oldToken = readTokenAsMapParams(token);
        if(oldToken != null){
            return createTokenByParams(oldToken);
        }else {
            return null;
        }
    }

    /**
     * 根据key值获取保存在token中的信息
     * @param key
     * @return String类型
     */
    public String getTokenValue(String key){
        Object value = this.getTokenValueAsObject(key);
        if(value != null){
            return String.valueOf(value);
        }
        return null;
    }

    /**
     * 获取token中保存的信息
     * @param key
     * @return Object类型
     */
    public Object getTokenValueAsObject(String key){
        String token = this.getToken();
        if(token != null) {
            Claims claims = this.readTokenAsMapParams(token);
            return claims.get(key);
        }
        return null;
    }

    /**
     * 从token中获取用户名信息
     * @return
     */
    public String getUsername(){
        String token = this.getToken();
        if(token != null) {
            Claims claims = this.readTokenAsMapParams(token);
            return (String) claims.get(USERNAME);

        }else {
            return null;
        }
    }

    /**
     * 获取用户id
     * @return
     */
    public Long getUserId(){
        String token = this.getToken();
        if(token != null){
            Claims claims = this.readTokenAsMapParams(token);
            return Long.valueOf(String.valueOf(claims.get(USER_ID)));
        }
        return null;
    }

    /**
     * 获取token
     * @return
     */
    public String getToken(){
        HttpServletRequest request = WebUtil.getRequest();
        return request.getHeader(AuthConstant.HEADER_STRING);
    }
}
