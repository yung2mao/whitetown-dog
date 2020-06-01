package cn.whitetown.dogbase.exception;

import cn.whitetown.dogbase.domain.vo.ResponseData;
import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.util.Charsets;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有异常处理类
 * @author GrainRain
 * @date 2020/05/24 11:48
 **/
@Component
public class ControllerExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        ServletOutputStream out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
             out = response.getOutputStream();
            if (e instanceof NullPointerException){
                ResponseData nullResult = ResponseData.fail(ResponseStatusEnum.ERROR_PARAMS);
                out.write(nullResult.toString().getBytes(Charsets.UTF_8));
            }else if(e instanceof CustomException) {
                CustomException ex = (CustomException) e;
                ResponseData failResult = ResponseData.getInstance(ex.getStatusEnum(), ex.getMessage());
                out.write(failResult.toString().getBytes(Charsets.UTF_8));
            }else {
                ResponseData failResult = ResponseData.fail(ResponseStatusEnum.FAIL);
                out.write(failResult.toString().getBytes(Charsets.UTF_8));
            }
            out.flush();
        }catch (Exception ee){
            ee.printStackTrace();
        }

        return new ModelAndView();
    }
}
