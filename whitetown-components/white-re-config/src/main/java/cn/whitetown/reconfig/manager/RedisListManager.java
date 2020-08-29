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
     * 保存一个list到redis
     * @param key
     * @param list
     * @param timeout
     * @param timeUnit
     */
    <T> void saveList(String key, List<T> list, Long timeout, TimeUnit timeUnit);


}
