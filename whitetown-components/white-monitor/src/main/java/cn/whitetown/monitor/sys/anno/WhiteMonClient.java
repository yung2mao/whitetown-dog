package cn.whitetown.monitor.sys.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 客户端注解
 * @author taixian
 * @date 2020/08/05
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WhiteMonClient {
    String type() default MonAnnotation.CLI_DEFAULT;
}
