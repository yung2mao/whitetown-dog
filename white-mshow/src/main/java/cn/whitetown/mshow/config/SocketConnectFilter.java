package cn.whitetown.mshow.config;

import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.mshow.manager.SocketCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author taixian
 * @date 2020/08/25
 **/
@Component
@WebFilter(urlPatterns = "/ws/white", filterName = "websocket-connect")
@Order(DogBaseConstant.SOCKET_CONNECT_FILTER_LEVEL)
public class SocketConnectFilter implements Filter {

    @Autowired
    private SocketCache socketCache;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest)request,
                (HttpServletResponse)response,filterChain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = request.getHeader("Sec-WebSocket-Protocol");
        response.setHeader("Sec-WebSocket-Protocol",token);
        String flag = "randomId";
        String randomId = request.getParameter(flag);
        //TODO:解析token
        socketCache.saveConnectUser(randomId,3343213L);
        filterChain.doFilter(request,response);
    }
}

