package cn.whitetown.weblog.collect;

import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author taixian
 * @date 2020/08/10
 **/
@WebFilter
@Order(DogBaseConstant.LOG_FILTER_LEVEL)
@Component
public class OpLogCollectFilter implements Filter {

    private Logger sysLogger = LogConstants.SYS_LOGGER;
    private Logger opLogger = LogConstants.OP_BASE_LOGGER;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private SnowIDCreateUtil idCreateUtil;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        this.doFilter(request,response,filterChain);
    }

    @Override
    public void destroy() {

    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) {
        long logId = idCreateUtil.getSnowId();
        request.setAttribute(LogConstants.TRACE_ID,logId);
        StringBuffer logBuffer = this.beforeDoFilter(logId, request);
        try {
            if(WebUtil.OPTIONS.equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request,response);
                return;
            }
            filterChain.doFilter(request,response);
            opLogger.info(afterDoFilter(logBuffer,response));
        } catch (Exception e) {
            opLogger.error(afterDoFilter(logBuffer,response));
            sysLogger.error(e.getMessage());
        }
    }

    private StringBuffer beforeDoFilter(Long logId, HttpServletRequest request) {
        StringBuffer logBuffer = new StringBuffer();
        Long userId = null;
        try {
            userId = tokenUtil.getUserId();
        }catch (Exception e){}
        //logId | userId | requestTime |  uri | clientIp | browser | status | responseTime
        return logBuffer.append(logId).append("|")
                .append(userId).append("|")
                .append(System.currentTimeMillis()).append("|")
                .append(request.getRequestURI()).append("|")
                .append(WebUtil.getClientIP(request)).append("|")
                .append(WebUtil.getBrowser(request)).append("|");
    }

    private String afterDoFilter(StringBuffer logBuffer, HttpServletResponse response) {
        return logBuffer.append(response.getStatus()).append("|")
                .append(System.currentTimeMillis()).append("|")
                .toString();
    }
}
