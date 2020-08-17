package cn.whitetown.dogbase.common.util;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author taixian
 * @date 2020/08/17
 **/
@FunctionalInterface
public interface WhiteFunc<T,R> extends Function<T, R>, Serializable {
}
