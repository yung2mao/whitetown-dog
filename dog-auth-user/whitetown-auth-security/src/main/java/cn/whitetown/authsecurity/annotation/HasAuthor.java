package cn.whitetown.authsecurity.annotation;

import org.mapstruct.TargetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author taixian
 * @date 2020/07/20
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HasAuthor {
    String value() default "top";
    String type();
}
