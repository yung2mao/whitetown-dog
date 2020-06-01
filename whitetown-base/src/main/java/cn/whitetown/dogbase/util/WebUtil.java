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
     * @param expireTime
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

    public static void addCookie(String cookieName,String cookieValue){
        addCookie(cookieName,cookieValue,-2);
    }
}
