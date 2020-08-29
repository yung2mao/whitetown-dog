package cn.whitetown.reconfig.manager.wiml;

import cn.whitetown.reconfig.manager.LockManager;
import cn.whitetown.reconfig.manager.RedisBaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁实现
 * @author taixian
 * @date 2020/08/29
 **/
@Service
@Lazy
public class LockManagerImpl implements LockManager {

    @Autowired
    private RedisBaseManager baseManager;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean addLock(String lockName, String lockId, Long expireMills) {
        return baseManager.putIfNotExists(lockName, lockId, expireMills, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean unLock(String lockName, String lockId) {
        return baseManager.deleteIfEquals(lockName,lockId);
    }

    @Override
    public void initPermitNum(String lockName, Long totalOrders, Long expireSecond) {
        if(expireSecond == null) {
            //TODO: exception
        }
        ValueOperations<String, String> stringOperation = redisTemplate.opsForValue();
        stringOperation.set(lockName,String.valueOf(totalOrders),expireSecond,TimeUnit.SECONDS);
    }

    @Override
    public boolean getOneLock(String lockName) {
        Long total = redisTemplate.opsForValue().decrement(lockName);
        System.out.println(total);
        assert total != null;
        return total >= 0;
    }
}
