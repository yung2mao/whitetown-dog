package cn.whitetown.reconfig.manager.wiml;

import cn.whitetown.reconfig.manager.RedisBaseManager;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis基本操作实现
 * @author taixian
 * @date 2020/08/29
 **/
@Component
public class RedisBaseManagerImpl implements RedisBaseManager {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public <T> T get(String key, Class<T> valueClass) {
        String result = redisTemplate.opsForValue().get(key);
        if(result == null) {
            return null;
        }
        if(valueClass == String.class) {
            return valueClass.cast(result);
        }
        return JSON.parseObject(result,valueClass);
    }

    @Override
    public <T> void save(String key, T value, Long expireSecond) {
        String data = JSON.toJSONString(value);
        assert key != null && expireSecond != null;
        redisTemplate.opsForValue().set(key,data,expireSecond, TimeUnit.SECONDS);
    }

    @Override
    public <T> boolean putIfNotExists(String key, T value, Long timeout, TimeUnit timeUnit) {
        String data = JSON.toJSONString(value);
        assert key != null && timeout != null && timeUnit != null;
        return redisTemplate.opsForValue().setIfAbsent(key,data,timeout,timeUnit);
    }

    @Override
    public <T> void expire(String key, Long expireSeconds) {
        assert key != null && expireSeconds != null;
        redisTemplate.expire(key,expireSeconds,TimeUnit.SECONDS);
    }

    @Override
    public <T> void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public <T> boolean deleteIfEquals(String key, T value) {
        boolean exists = redisTemplate.opsForValue().get(key) != null;
        if(!exists) {
            return true;
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) " +
                "else return 0 end";
        redisScript.setScriptText(script);
        List<String> keys = new ArrayList<>();

        keys.add(key);
        String data = JSON.toJSONString(value);
        Long result = redisTemplate.execute(redisScript, keys, data);
        assert result != null;
        return result > 0L;
    }
}
