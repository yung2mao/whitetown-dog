package cn.whitetown.monitor.app.collect;

import org.apache.catalina.connector.RequestFacade;
import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author taixian
 * @date 2020/07/31
 **/
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*",filterName = "globalMonitorFilter")
@Order(Ordered.HIGHEST_PRECEDENCE+1)
public class MonitorFilter extends OncePerRequestFilter {

    Logger logger = Logger.getLogger(MonitorFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request,response);
        long endTime = System.currentTimeMillis();
        String proxyFlag = "$";
        if(!request.toString().contains(proxyFlag)) {
            logger.warn("response time > " + (endTime - startTime));
        }
    }
}
