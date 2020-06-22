package cn.whitetown.dogbase.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类上添加此注解，表示当前类的属性允许被作为条件在数据库进行检索
 * @author GrainRain
 * @date 2020/06/20 22:09
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryTable {
}
