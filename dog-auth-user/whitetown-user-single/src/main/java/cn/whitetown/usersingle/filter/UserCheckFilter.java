package cn.whitetown.usersingle.filter;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.usersingle.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author GrainRain
 * @date 2020/06/12 21:27
 **/
@WebFilter("loginFilter")
@Component
public class UserCheckFilter implements Filter {
    @Autowired
    private LoginService loginService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 登录状态校验过滤器
     * 放行登录接口，其余地址需要登录后操作
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setContentType("text/html;charset=UTF-8");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String requestURI = request.getRequestURI();
        if (!requestURI.contains("/erus/")) {
            String token = WebUtil.getCookieValue(AuthConstant.TOKEN_COOKIE_NAME, request);
            if (DataCheckUtil.checkTextNullBool(token)) {
                this.writeAndFlush(servletResponse, ResponseStatusEnum.NOT_LOGIN);
                return;
            }

            String newToken = loginService.checkLogin(token);
            //如果需要颁发新的token，那么就刷新cookie中的token
            if (newToken != null) {
                try {
                    WebUtil.addCookie(AuthConstant.TOKEN_COOKIE_NAME, token, AuthConstant.TOKEN_EXPIRE);
                } catch (WhResException e) {
                    this.writeAndFlush(servletResponse, ResponseStatusEnum.TOKEN_ERROR);
                    return;
                }
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    public void writeAndFlush(ServletResponse servletResponse,ResponseStatusEnum statusEnum) throws IOException {
        PrintWriter writer = servletResponse.getWriter();
        ResponseData result = ResponseData.fail(statusEnum);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    @Override
    public void destroy() {

    }
}
