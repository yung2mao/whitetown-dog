package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.monitor.config.MonConfConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GrainRain
 * @date 2020/05/30 09:23
 **/
public class WhiteJwtTokenUtil implements JwtTokenUtil {

    private Logger logger = MonConfConstants.logger;
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
    private final String TOKEN_PREFIX;
    /**
     * 请求头名称
     */
    private final String HEADER_STRING;

    public WhiteJwtTokenUtil(String secret, long expire,String TOKEN_PREFIX,String HEADER_STRING) {
        this.EXPIRATION_TIME = expire;
        this.SECRET = secret;
        this.TOKEN_PREFIX = TOKEN_PREFIX;
        this.HEADER_STRING = HEADER_STRING;
        logger.info("正在初始化Jwt util，expire time="+expire);
    }

    @Override
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

    @Override
    public Claims readTokenAsMapParams(String token) {
        if (token == null) {
            return new DefaultClaims(new HashMap<>(0));
        }
        Claims body = null;
        try {
            body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
        }catch (ExpiredJwtException expiredException) {
            logger.debug(expiredException.getMessage());
            throw new CustomException(ResponseStatusEnum.TOKEN_EXPIRED);
        }catch (Exception e) {
            logger.debug(e.getMessage());
            throw new CustomException(ResponseStatusEnum.TOKEN_ERROR);
        }
        return body == null ? new DefaultClaims(new HashMap<>(0)) : body;
    }
    @Override
    public String updateToken(String token){
        Map<String, Object> oldToken = readTokenAsMapParams(token);
        if(oldToken != null){
            return createTokenByParams(oldToken);
        }else {
            return null;
        }
    }

    @Override
    public String getTokenValue(String key){
        Object value = this.getTokenValueAsObject(key);
        if(value != null){
            return String.valueOf(value);
        }
        return null;
    }

    @Override
    public Object getTokenValueAsObject(String key){
        String token = this.getToken();
        if(token != null) {
            Claims claims = this.readTokenAsMapParams(token);
            return claims.get(key);
        }
        return null;
    }

    @Override
    public String getUsername(){
        String token = this.getToken();
        if(token != null) {
            Claims claims = this.readTokenAsMapParams(token);
            return claims.get(USERNAME,String.class);
        }else {
            return null;
        }
    }

    @Override
    public Long getUserId(){
        String token = this.getToken();
        if(token != null){
            Claims claims = this.readTokenAsMapParams(token);
            return claims.get(USER_ID,Long.class);
        }
        return null;
    }

    @Override
    public String getToken(){
        HttpServletRequest request = WebUtil.getRequest();
        String header = request.getHeader(HEADER_STRING);
        if(header == null) {
            return null;
        }
        return header.startsWith(TOKEN_PREFIX) ? header : null;
    }
}
