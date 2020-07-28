package cn.whitetown.usersecurity.filtet;

import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authea.manager.TokenCheckManager;
import cn.whitetown.authea.service.WhiteUserDetailService;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * token过滤器实现类
 * @author taixian
 * @date 2020/07/27
 **/
@Component
public class TokenCheckFilterManager extends TokenCheckManager {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WhiteUserDetailService jwtUserDetailsService;

    @Override
    public boolean shouldFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                return false;
            }catch (Exception e){
                throw e;
            }
            if (userDetails!=null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        return true;
    }

    @Override
    public void afterFilter(HttpServletRequest request, HttpServletResponse response) {
    }
}
