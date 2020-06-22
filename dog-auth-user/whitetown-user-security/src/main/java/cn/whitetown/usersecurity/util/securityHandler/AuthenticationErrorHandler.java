package cn.whitetown.usersecurity.util.securityHandler;

import cn.whitetown.dogbase.common.entity.vo.ResponseData;
import cn.whitetown.dogbase.common.entity.vo.ResponseStatusEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 *  认证失败处理器
 * @author GrainRain
 * @date 2020/06/13 16:09
 **/
public class AuthenticationErrorHandler implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResponseData result = ResponseData.fail(ResponseStatusEnum.NOT_LOGIN);
        PrintWriter writer = response.getWriter();
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }
}
