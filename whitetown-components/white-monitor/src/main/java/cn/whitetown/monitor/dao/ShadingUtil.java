package cn.whitetown.monitor.dao;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 分片键工具类
 * @author taixian
 * @date 2020/08/07
 **/
public interface ShadingUtil {
    /**
     * 范围分片
     * 需在entity中指定分片键
     * @param entity
     * @return
     * @throws IllegalAccessException
     */
    public Integer scopeSharding(Object entity) throws IllegalAccessException;

    /**
     * 哈希取余分片
     * 需在entity中指定分片键
     * @param entity
     * @throws IllegalArgumentException
     * @return
     */
    public Integer hashSharding(Object entity) throws IllegalAccessException;

    /**
     * 范围分片
     * 根据key值
     * @param keys
     * @return
     */
    Integer scopeSharding(Object ... keys);
}
