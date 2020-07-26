package cn.whitetown.authsecurity.annotation;

import cn.whitetown.authsecurity.modo.WhiteControlType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author taixian
 * @date 2020/07/24
 **/
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WhiteAuthAnnotation {
    String type() default WhiteControlType.PERMIT_ALL;
    String[] value() default {};
}
