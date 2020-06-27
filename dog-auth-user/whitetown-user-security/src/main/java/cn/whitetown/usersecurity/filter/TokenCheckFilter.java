package cn.whitetown.usersecurity.filter;

import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.entity.vo.ResponseData;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.usersecurity.service.impl.UserDetailServiceImpl;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * token校验过滤器
 * @author GrainRain
 * @date 2020/06/13 15:46
 **/
@Component
public class TokenCheckFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public void setJwtUtilHelper(JwtTokenUtil jwt){
        this.jwtTokenUtil = jwt;
    }

    @Autowired
    private UserDetailServiceImpl jwtUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = jwtTokenUtil.getUsername();
        if(!DataCheckUtil.checkTextNullBool(username) && SecurityContextHolder.getContext().getAuthentication() == null){
            //用户信息提取
            UserDetails userDetails = null;
            try {
                userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            }catch (MalformedJwtException | CustomException e){
                ResponseData responseData = null;
                if(e instanceof CustomException){
                    responseData = ResponseData.fail(((CustomException) e).getStatusEnum());
                }else {
                    responseData = ResponseData.fail(ResponseStatusEnum.ERROR_PARAMS);
                }
                PrintWriter writer = response.getWriter();
                writer.write(responseData.toString());
                writer.flush();
                writer.close();
                return;
            }catch (Exception e){
                throw e;
            }
            if (userDetails!=null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }
}
