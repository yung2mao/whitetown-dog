package cn.whitetown.reconfig.manager;

import java.util.List;
import java.util.Map;

/**
 * hash结构操作
 * @author taixian
 * @date 2020/08/29
 **/
public interface RedisHashManager {

    /**
     * 获取hash结构中field对应value
     * @param key
     * @param field
     * @param valueClass
     * @param <T>
     * @return
     */
    <T> T get(String key, String field, Class<T> valueClass);

    /**
     * 获取hash中所有数据
     * @param key
     * @return
     */
    Map<String,String> getAll(String key);

    /**
     * 获取hash中指定的field
     * @param key
     * @param fields
     * @return
     */
    List<String> getCusFields(String key, String ... fields);

    /**
     * 存储为hash结构
     * @param key
     * @param field hash结构field
     * @param value
     * @param expireSeconds
     * @param <T>
     */
    <T> void save(String key, String field, T value, Long expireSeconds);

    /**
     * 将map保存为hash结构
     * @param key
     * @param map
     * @param expireSeconds
     * @param <T>
     */
    <T> void saveEntries(String key, Map<String, T> map, Long expireSeconds);

    /**
     * 删除hash结构中某个field
     * @param key
     * @param fields
     * @param <T>
     */
    <T> void deleteCusFields(String key, String ... fields);
}
