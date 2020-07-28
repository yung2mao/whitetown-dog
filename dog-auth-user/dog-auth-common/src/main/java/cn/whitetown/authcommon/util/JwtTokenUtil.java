package cn.whitetown.authcommon.util;

import io.jsonwebtoken.Claims;

import java.util.Map;

/**
 * @author taixian
 * @date 2020/07/27
 **/
public interface JwtTokenUtil {
    /**
     * 生成Token
     * @param claims 需要保存在token的参数，如用户名username，用户ID
     * @return
     */
    public String createTokenByParams(Map<String, Object> claims);

    /**
     * 解析token
     * @param token
     * @return
     */
    public Claims readTokenAsMapParams(String token);

    /**
     * token更新
     * @param token
     * @return
     */
    public String updateToken(String token);

    /**
     * 根据key值获取保存在token中的信息
     * @param key
     * @return String类型
     */
    public String getTokenValue(String key);

    /**
     * 获取token中保存的信息
     * @param key
     * @return Object类型
     */
    public Object getTokenValueAsObject(String key);

    /**
     * 从token中获取用户名信息
     * @return
     */
    public String getUsername();

    /**
     * 获取用户id
     * @return
     */
    public Long getUserId();

    /**
     * 获取token
     * @return
     */
    public String getToken();
}
