package cn.whitetown.dogbase.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在属性上，用于限定检索方式
 * @author GrainRain
 * @date 2020/06/20 22:10
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryField {
    /**
     * 默认情况下，表示遵守驼峰命名规则进行转换
     * @return
     */
    String value() default "";

    /**
     * 是否忽略，默认情况下不会忽略该字段
     * @return
     */
    boolean ignore() default false;

    /**
     * 操作类别，默认情况下是相等，可以输入gt/ge/lt/le
     * @return
     */
    String operation() default "eq";

}
