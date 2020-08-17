package cn.whitetown.esconfig.annotation;

import cn.whitetown.esconfig.config.EsConfigEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性信息
 * @author taixian
 * @date 2020/08/16
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsFieldConfig {
    String name() default "";

    /**
     * 冒号分隔配置的key和value
     * @return
     */
    EsConfigEnum[] config() default {EsConfigEnum.TEXT};
}
