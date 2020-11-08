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
    String createTokenByParams(Map<String, Object> claims);

    /**
     * 解析token
     * @param token
     * @return
     */
    Claims readTokenAsMapParams(String token);

    /**
     * token更新
     * @param token
     * @return
     */
    String updateToken(String token);

    /**
     * 根据key值获取保存在token中的信息
     * @param key
     * @return String类型
     */
    String getTokenValue(String key);

    /**
     * 获取token中保存的信息
     * @param key
     * @return Object类型
     */
    Object getTokenValueAsObject(String key);

    /**
     * 从token中获取用户名信息
     * @return
     */
    String getUsername();

    /**
     * 获取用户id
     * @return
     */
    Long getUserId();

    /**
     * 获取token
     * @return
     */
    String getToken();
}
