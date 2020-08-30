package cn.whitetown.reconfig.manager;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * list操作
 * @author taixian
 * @date 2020/08/29
 **/
public interface RedisListManager {

    /**
     * 从list取出一个元素
     * @param key
     * @param valueClass
     * @param <T>
     * @return
     */
    <T> T popOldestEle(String key, Class<T> valueClass);

    /**
     * 获取最新的一个元素
     * @param key
     * @param valueClass
     * @param <T>
     * @return
     */
    <T> T popRecentEle(String key, Class<T> valueClass);

    /**
     * 获取截取list的一段元素
     * @param key
     * @param valueClass
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    <T> List<T> subList(String key,Class<T> valueClass, Long start, Long end);

    /**
     * 保存一个list到redis
     * @param key
     * @param list
     * @param timeout
     * @param timeUnit
     */
    <T> void saveList(String key, List<T> list, Long timeout, TimeUnit timeUnit);

    /**
     * 在list中添加一个元素
     * @param key
     * @param value
     * @param <T>
     */
    <T> void addOne(String key, T value);

    /**
     * 保留指定范围内元素
     * @param key
     * @param start
     * @param end
     */
    void trim(String key, long start, long end);
}
