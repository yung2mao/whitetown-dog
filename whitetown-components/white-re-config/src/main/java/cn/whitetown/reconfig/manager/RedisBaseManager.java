package cn.whitetown.reconfig.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis基本操作
 * @author taixian
 * @date 2020/08/29
 **/
public interface RedisBaseManager {

    /**
     * 获取key对应的value，转换为指定对象
     * @param key
     * @param valueClass
     * @param <T>
     * @return
     */
    <T> T get(String key,Class<T> valueClass);

    /**
     * 保存元素
     * @param key
     * @param value
     * @param expireSeconds
     * @param <T>
     */
    <T> void save(String key, T value, Long expireSeconds);

    /**
     * 添加元素 - 如果不存在
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @param <T>
     * @return 保存是否成功
     */
    <T> boolean putIfNotExists(String key, T value, Long timeout, TimeUnit timeUnit);

    /**
     * 为key设置过期时间
     * @param key
     * @param expireSeconds
     * @param <T>
     */
    <T> void expire(String key, Long expireSeconds);

    /**
     * 删除元素
     * @param key
     * @param <T>
     */
    <T> void delete(String key);

    /**
     * value相等时才会删除
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    <T> boolean deleteIfEquals(String key, T value);

}
