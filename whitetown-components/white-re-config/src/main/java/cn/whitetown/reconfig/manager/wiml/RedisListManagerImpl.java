package cn.whitetown.reconfig.manager.wiml;

import cn.whitetown.reconfig.manager.RedisListManager;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    public <T> void saveList(String key, List<T> list, Long timeout, TimeUnit timeUnit) {
        List<String> dataList = list.stream().filter(Objects::nonNull)
                .map(JSON::toJSONString).collect(Collectors.toList());
        redisTemplate.opsForList().leftPushAll(key,dataList);
        assert timeout != null;
        redisTemplate.expire(key, timeout, timeUnit);
    }
}
