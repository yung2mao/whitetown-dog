package cn.whitetown.reconfig.manager.wiml;

import cn.whitetown.reconfig.manager.RedisHashManager;
import cn.whitetown.reconfig.manager.RedisBaseManager;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author taixian
 * @date 2020/08/29
 **/
@Service
@Lazy
public class RedisHashManagerImpl implements RedisHashManager {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private RedisBaseManager baseManager;

    @Override
    public <T> T get(String key, String field, Class<T> valueClass) {
        Object result = redisTemplate.opsForHash().get(key, field);
        if(result == null) { return null; }
        return JSON.parseObject(String.valueOf(result),valueClass);
    }

    @Override
    public Map<String,String> getAll(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<String,String> map = new HashMap<>(2);
        entries.keySet().forEach(k-> map.put(String.valueOf(k), String.valueOf(entries.get(k))));
        return map;
    }

    @Override
    public List<String> getCusFields(String key, String ... fields) {
        if(fields == null) { return new ArrayList<>(); }
        List<Object> fieldList = new LinkedList<>(Arrays.asList(fields));
        List<Object> results = redisTemplate.opsForHash().multiGet(key, fieldList);
        return results.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    public <T> void save(String key, String field, T value, Long expireSeconds) {
        redisTemplate.opsForHash().put(key,
                field, JSON.toJSONString(value));
        if(expireSeconds != null && expireSeconds > 0) {
            redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }
    }

    @Override
    public <T> void saveEntries(String key, Map<String, T> map, Long expireSeconds) {
        redisTemplate.opsForHash().putAll(key,map);
        if(expireSeconds != null && expireSeconds > 0) {
            redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }
    }

    @Override
    public <T> void deleteCusFields(String key, String ... fields) {
        if (fields == null || fields.length < 1) {
            return;
        }
        redisTemplate.opsForHash().delete(key, (Object) fields);
    }
}
