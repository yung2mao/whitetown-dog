package cn.whitetown.usersecurity.filter;

import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authea.manager.TokenCheckManager;
import cn.whitetown.authea.modo.WhiteSecurityUser;
import cn.whitetown.authea.service.WhiteUserDetailService;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.logbase.config.LogConstants;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private Logger sysLogger = LogConstants.SYS_LOGGER;
    private Logger opLogger = LogConstants.OP_BASE_LOGGER;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WhiteUserDetailService userDetailService;

    @Override
    public boolean shouldRelease(HttpServletRequest request, HttpServletResponse response) {
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            return true;
        }
        String username = null;
        UserDetails userDetails = null;
        try {
            username = jwtTokenUtil.getUsername();
            if(username == null) {
                return true;
            }
            userDetails = userDetailService.loadUserByUsername(username);
        }catch (MalformedJwtException | WhResException e){
            ResponseData responseData = null;
            if(e instanceof WhResException){
                responseData = ResponseData.fail(((WhResException) e).getStatusEnum());
            }else {
                responseData = ResponseData.fail(ResponseStatusEnum.ERROR_PARAMS);
            }
            this.writeError(response,responseData);
            return false;
        }catch (Exception e){
            opLogger.debug(e.getMessage());
            return false;
        }
        if (userDetails != null) {
            Authentication authentication = null;
            if(userDetails instanceof WhiteSecurityUser) {
                authentication = ((WhiteSecurityUser) userDetails).getAuthentication();
            }
            authentication = authentication != null ? authentication : new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return true;
    }

    @Override
    public void afterFilter(HttpServletRequest request, HttpServletResponse response) {
    }

    private void writeError(HttpServletResponse response,ResponseData responseData) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(responseData.toString());
            writer.flush();
        } catch (IOException ex) {
            sysLogger.error(ex.getMessage());
        }finally {
            if(writer != null) {
                writer.close();
            }
            writer = null;
        }
    }
}
