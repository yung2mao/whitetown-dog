package cn.whitetown.dogbase.exception;

import cn.whitetown.dogbase.domain.vo.ResponseData;
import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.ConstraintViolationException;
/**
 * 所有异常处理类
 * @author GrainRain
 * @date 2020/05/24 11:48
 **/
@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler{
    /**
     * 参数错误处理，空指针异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            NullPointerException.class})
    public ResponseData MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return ResponseData.build(ResponseStatusEnum.ERROR_PARAMS.getStatus(),e.getMessage(),null);
    }

    /**
     * 自定义异常类的处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseData customException(CustomException e){
        return ResponseData.fail(e.getStatusEnum());
    }

    /**
     * 剩余其他异常捕获
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseData exceptionHandler(Exception e){
        e.printStackTrace();
        return ResponseData.fail(ResponseStatusEnum.SERVER_ERROR);
    }
}
