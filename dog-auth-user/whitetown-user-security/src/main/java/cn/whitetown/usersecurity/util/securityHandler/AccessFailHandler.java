package cn.whitetown.usersecurity.util.securityHandler;

import cn.whitetown.dogbase.domain.vo.ResponseData;
import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理没有权限的访问
 * @author GrainRain
 * @date 2020/06/13 16:18
 **/
public class AccessFailHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ResponseData result = ResponseData.fail(ResponseStatusEnum.NO_PERMITION);
        PrintWriter writer = response.getWriter();
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }
}
