package cn.whitetown.dogbase.util;

import com.sun.xml.internal.ws.client.ResponseContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web相关的工具
 * @author GrainRain
 * @date 2020/05/30 09:45
 **/
public class WebUtil {
    private WebUtil(){}

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
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
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Cookie cookie = new Cookie(cookieName,cookieValue);
        cookie.setPath(request.getContextPath()+"/");
        if(expireTime >= 0) {
            cookie.setMaxAge(expireTime);
        }
        response.addCookie(cookie);
    }

    /**
     * 添加cookie，不指定超时时间，默认会话级别
     * @param cookieName
     * @param cookieValue
     */
    public static void addCookie(String cookieName,String cookieValue){
        addCookie(cookieName,cookieValue,-2);
    }

    /**
     * 获取访问客户的IP地址
     * @param request
     * @return
     */
    public static String getClientIP(HttpServletRequest request){
        String localIP = "127.0.0.1";
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

        return "0:0:0:0:0:0:0:1".equals(ip) ? localIP : ip;
    }
}
