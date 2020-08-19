package cn.whitetown.dogbase.common.exception;

import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
/**
 * 所有异常处理类
 * 同时对日志进行记录
 * @author GrainRain
 * @date 2020/05/24 11:48
 **/
@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler{

    private Logger logger = LogConstants.OP_DETAIL_LOGGER;

    /**
     * 参数错误处理，空指针异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class,ConstraintViolationException.class,
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            NullPointerException.class})
    public ResponseData methodArgumentNotValidException(Exception e) {
        String message = e.getMessage();
        int keepLen  = 100;
        if(message != null && message.length() > keepLen) {
            message = message.substring(0,keepLen);
        }
        ResponseData failResult = ResponseData.build(ResponseStatusEnum.ERROR_PARAMS.getStatus(), message, null);
        this.errorLog(failResult);
        return failResult;
    }

    /**
     * 自定义异常类的处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseData customException(CustomException e){
        ResponseData failResult = ResponseData.fail(e.getStatusEnum());
        this.errorLog(failResult);
        return failResult;
    }

    /**
     * 访问方式异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseData httpMethodException(HttpRequestMethodNotSupportedException e){
        ResponseData<Object> failResult = ResponseData.build(400, e.getMessage(), null);
        this.errorLog(failResult);
        return failResult;
    }
    /**
     * 剩余其他异常捕获
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseData exceptionHandler(Exception e){
        e.printStackTrace();
        ResponseData failResult = ResponseData.fail(ResponseStatusEnum.SERVER_ERROR);
        failResult.setStatusName(e.getMessage());
        this.errorLog(failResult);
        failResult.setStatusName(ResponseStatusEnum.ERROR_PARAMS.getStatusName());
        return failResult;
    }

    private Object getTraceId() {
        HttpServletRequest request = WebUtil.getRequest();
        return request.getAttribute(LogConstants.TRACE_ID);
    }

    private void errorLog(ResponseData failResult) {
        logger.error(getTraceId() + "|" + System.currentTimeMillis() + "|" + failResult);
    }
}
