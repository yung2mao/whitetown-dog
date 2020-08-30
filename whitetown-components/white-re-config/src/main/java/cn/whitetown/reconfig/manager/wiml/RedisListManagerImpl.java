package cn.whitetown.reconfig.manager.wiml;

import cn.whitetown.reconfig.manager.RedisListManager;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis list操作
 * @author taixian
 * @date 2020/08/29
 **/
@Service
@Lazy
public class RedisListManagerImpl implements RedisListManager {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public <T> T popOldestEle(String key, Class<T> valueClass) {
        String result = redisTemplate.opsForList().rightPop(key);
        return result == null ? null : JSON.parseObject(result,valueClass);
    }

    @Override
    public <T> T popRecentEle(String key, Class<T> valueClass) {
        String result = redisTemplate.opsForList().leftPop(key);
        return result == null ? null : JSON.parseObject(result,valueClass);
    }

    @Override
    public <T> List<T> subList(String key, Class<T> valueClass, Long start, Long end) {
        List<String> resultList = redisTemplate.opsForList().range(key, start, end);
        if(resultList == null) {
            return new ArrayList<>();
        }
        return resultList.stream()
                .map(ele->JSON.parseObject(ele,valueClass))
                .collect(Collectors.toList());
    }

    @Override
    public <T> void saveList(String key, List<T> list, Long timeout, TimeUnit timeUnit) {
        List<String> dataList = list.stream().filter(Objects::nonNull)
                .map(JSON::toJSONString).collect(Collectors.toList());
        redisTemplate.opsForList().leftPushAll(key,dataList);
        assert timeout != null;
        redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public <T> void addOne(String key, T value) {
        String data = JSON.toJSONString(value);
        redisTemplate.opsForList().leftPush(key, data);
    }

    @Override
    public void trim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key,start,end);
    }
}
