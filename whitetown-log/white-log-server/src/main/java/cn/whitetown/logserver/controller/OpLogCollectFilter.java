package cn.whitetown.logserver.controller;

import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author taixian
 * @date 2020/08/10
 **/
@WebFilter
@Order(DogBaseConstant.LOG_FILTER_LEVEL)
@Component
public class OpLogCollectFilter implements Filter {

    private Logger sysLogger = LogConstants.SYS_LOGGER;
    private Logger opLogger = LogConstants.OP_LOGGER;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        this.doFilter(request,response,filterChain);
    }

    @Override
    public void destroy() {

    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) throws IOException, ServletException {
        if(WebUtil.OPTIONS.equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request,response);
            return;
        }
        String requestURI = request.getRequestURI();

        String ip = WebUtil.getClientIP(request);
        long startTime = System.currentTimeMillis();
        System.out.println(requestURI);
        String requestParam = WebUtil.getRequestParams(request);
        System.out.println(requestParam);
        try {
            filterChain.doFilter(request,response);
        }catch (IOException|ServletException e){
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

    }
}
