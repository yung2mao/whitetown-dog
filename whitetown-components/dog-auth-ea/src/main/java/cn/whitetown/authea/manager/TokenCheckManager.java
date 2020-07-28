package cn.whitetown.authea.manager;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token校验过滤器
 * @author GrainRain
 * @date 2020/06/13 15:46
 **/
public abstract class TokenCheckManager extends OncePerRequestFilter {

    /**
     * 过滤前执行的方法
     * @param request
     * @param response
     * @return
     */
    public abstract boolean shouldFilter(HttpServletRequest request, HttpServletResponse response) throws IOException;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isRelease = this.shouldFilter(request,response);
        if(isRelease) {
            filterChain.doFilter(request, response);
            this.afterFilter(request,response);
        }
    }

    /**
     * 调用结束执行的方法
     * @param request
     * @param response
     */
    public abstract void afterFilter(HttpServletRequest request, HttpServletResponse response);
}
