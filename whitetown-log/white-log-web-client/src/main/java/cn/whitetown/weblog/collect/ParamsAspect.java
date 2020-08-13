package cn.whitetown.weblog.collect;

import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 获取各类参数的切面类
 * @author taixian
 * @date 2020/08/13
 **/
@Aspect
@Component
public class ParamsAspect {

    private Logger opDetailLogger = LogConstants.OP_DETAIL_LOGGER;

    @Pointcut("execution(public * cn.whitetown..*Controller.*(..))")
    private void cutMethod() {
    }

}
