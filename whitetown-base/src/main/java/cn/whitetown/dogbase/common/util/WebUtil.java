package cn.whitetown.dogbase.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Web相关的工具
 * @author GrainRain
 * @date 2020/05/30 09:45
 **/
public class WebUtil {

    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    public static final String PUT_METHOD = "PUT";
    public static final String DELETE_METHOD = "DELETE";
    public static final String OPTIONS = "OPTIONS";

    private WebUtil(){}

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    /**
     * 请求拼接的参数处理为map形式返回
     * @param queryString
     * @return
     */
    public static Map<String,String> requestString2Map(String queryString) {
        Map<String,String> result = new HashMap<>(1);
        if(queryString == null) {
            return result;
        }
        String[] entryArr = queryString.split("&");
        for(String entry : entryArr) {
            String[] keyValue = entry.split("=");
            String key = keyValue[0];
            String value = keyValue.length == 2 ? keyValue[1] : null;
            result.put(key,value);
        }
        return result;
    }

    /**
     * 获取访问资源路径
     * @return
     */
    public static String getUri(){
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getRequestURI();
    }

    /**
     * 获取request中的请求参数
     * @param request
     * @return
     */
    public static String getRequestParams(HttpServletRequest request) throws IOException {
        return getRequestParams(request,null);
    }

    public static String getRequestParams(HttpServletRequest request, byte[] paramBytes) throws IOException {
        if(request == null) {
            return null;
        }
        if(paramBytes == null) {
            paramBytes = new byte[1024];
        }
        String requestParams = request.getQueryString();
        if(requestParams != null) {
            return requestParams;
        }
        ServletInputStream in = request.getInputStream();
        int read = in.read(paramBytes);
        return read == -1 ? "" : new String(paramBytes,0,read);
    }

    /**
     * 获取指定名称的cookie value
     * @param name
     * @param request
     * @return
     */
    public static String getCookieValue(String name, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return null;
        }

        for(Cookie cookie:cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 添加cookie
     * @param cookieName cookie名称 - not null
     * @param cookieValue cookie value
     * @param expireTime 超时时间
     */
    public static void addCookie(String cookieName,String cookieValue,int expireTime){
        HttpServletRequest request = getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        Cookie cookie = new Cookie(cookieName,cookieValue);
        cookie.setPath(request.getContextPath()+"/");
        if(expireTime >= 0) {
            cookie.setMaxAge(expireTime);
        }
        assert response != null;
        response.addCookie(cookie);
    }

    /**
     * 添加cookie，不指定超时时间，默认会话级别
     * @param cookieName
     * @param cookieValue
     */
    public static void addCookie(String cookieName,String cookieValue){
        addCookie(cookieName,cookieValue,-1);
    }

    /**
     * 获取访问客户的IP地址
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request){
        String localIp = "127.0.0.1";
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? localIp : ip;
    }

    /**
     * 获取客户端浏览器类型
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String agent = request.getHeader("user-agent");
        return agent == null ? "" : agent;
    }

    /**
     * 基于IP,浏览器类型生成近似唯一ID作为sessionId
     * @return
     */
    public static String getCusSessionId(HttpServletRequest request){
        try {
            return getClientIp(request).trim() + getBrowser(request).hashCode();
        }catch (Exception e){
            return "";
        }
    }
}
