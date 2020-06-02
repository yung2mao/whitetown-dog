package cn.whitetown.dog.config;

import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author GrainRain
 * @date 2020/06/02 22:33
 **/
@Component
@WebFilter("cFilter")
public class CookieCorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("the cookie cors filter is started");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse re = (HttpServletResponse)response;
        re.setHeader("Access-Control-Allow-Credentials","true");
        re.setHeader("Access-Control-Allow-Origin","http://127.0.0.1:5500");
        filterChain.doFilter(servletRequest,response);
    }

    @Override
    public void destroy() {
        System.out.println("the cookie cors filter is destroy");
    }
}
