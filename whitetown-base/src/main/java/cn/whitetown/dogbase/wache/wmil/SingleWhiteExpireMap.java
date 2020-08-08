package cn.whitetown.dogbase.wache.wmil;

import cn.whitetown.dogbase.wache.WhiteExpireMap;
import cn.whitetown.dogbase.wache.excep.CapacityOutException;
import cn.whitetown.dogbase.wache.excep.ElementRemoveException;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 具有过期时间的key-value缓存结构 - 单实例结构
 * @author GrainRain
 * @date 2020/05/27 22:13
 **/
public class SingleWhiteExpireMap<K,V> implements WhiteExpireMap<K,V> {
    private ConcurrentHashMap<K,WhCacheKey> expireKeyMap;
    private ConcurrentHashMap<K,V> valueMap;

    private static final int MAX_CAPACITY = (2 << 15) - 1;
    private Lock lock = new ReentrantLock();

    public SingleWhiteExpireMap(){
        this.expireKeyMap = new ConcurrentHashMap<>(16);
        this.valueMap = new ConcurrentHashMap<>(32);
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
        expireKeyMap.clear();
        valueMap.clear();
    }

    @Override
    public V put(K key, V value){
        this.elementCheckOrRemove();
        return valueMap.put(key,value);
    }

    @Override
    public V put(K key, V value, long pExpire){
        if(pExpire<1){
            return value;
        }
        // if capacity is full , remove some value
        this.elementCheckOrRemove();
        try {
            WhCacheKey cacheKey = this.createCacheKey(key, pExpire);
            expireKeyMap.put(key,cacheKey);
            return valueMap.put(key,value);
        }catch (Exception e){
            try {
                expireKeyMap.remove(key);
                valueMap.remove(key);
                throw e;
            }catch (Exception e1){
                throw e1;
            }

        }
    }

    @Override
    public V putS(K key, V value, long expireSecond){
        this.elementCheckOrRemove();
        return this.put(key,value,1000 * expireSecond);
    }

    @Override
    public long pExpire(K key, long pExpire){
        if(pExpire<1){
            return -1;
        }
        V v = valueMap.get(key);
        if(v != null){
            WhCacheKey cacheKey = expireKeyMap.get(key);
            Long expireTime = cacheKey == null ? null : cacheKey.getExpireTime();
            if(expireTime != null){
                if(!this.isExpired(key)){
                    cacheKey.expireTime = System.currentTimeMillis() + pExpire;
                    expireKeyMap.put(key,cacheKey);
                    return System.currentTimeMillis()+pExpire;
                }else {
                    return -1;
                }
            }else {
                cacheKey = this.createCacheKey(key,pExpire);
                expireKeyMap.put(key,cacheKey);
                return System.currentTimeMillis()+pExpire;
            }
        }else {
            return -1;
        }
    }

    @Override
    public long sExpire(K key, long sExpire) {
        long pExpire = 1000 * sExpire;
        return this.pExpire(key,pExpire);
    }

    /**
     * get value
     * @param key
     * @return
     */
    @Override
    public V get(K key){
        WhCacheKey whCacheKey = expireKeyMap.get(key);
        if(whCacheKey != null){
            if(!this.isExpired(key)){
                whCacheKey.recentUseTime = System.currentTimeMillis();
                expireKeyMap.put(key,whCacheKey);
                return valueMap.get(key);
            }else {
                return null;
            }
        }else {
            return valueMap.get(key);
        }
    }

    /**
     * remove key-value
     * @param key
     * @return
     */
    @Override
    public V remove(K key){
        expireKeyMap.remove(key);
        return valueMap.remove(key);
    }

    /**
     * is contains key
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(K key){
        return this.get(key) != null;
    }

    @Override
    public boolean isExpired(K key) {
        if(expireKeyMap.get(key).getExpireTime() < System.currentTimeMillis()){
            this.remove(key);
            return true;
        }
        return false;
    }

    /**
     * reset data
     */
    @Override
    public void clean(){
        expireKeyMap.clear();
        valueMap.clear();
    }

    /**
     * if the cache capacity is full,
     * remove the least recently used data
     */
    @Override
    public void cleanIfFull() {
        this.expireScan();
        if(valueMap.size() < (MAX_CAPACITY >> 1)) {
            return;
        }
        List<WhCacheKey> keyList = expireKeyMap.values().stream()
                .sorted(Comparator.comparing(WhCacheKey::getRecentUseTime))
                .collect(Collectors.toList());
        int removeIndex = keyList.size() >> 2;
        for (int i = 0; i < removeIndex; i++) {
            this.remove(keyList.get(i).key);
        }
        keyList.clear();
        if(valueMap.size() >= MAX_CAPACITY) {
            throw new CapacityOutException();
        }
    }

    /**
     * 扫描具有定期时间的键值对并处理过期的数据
     */
    @Override
    public void expireScan(){
        for(K key: expireKeyMap.keySet()){
            if (isExpired(key)){
                expireKeyMap.remove(key);
                valueMap.remove(key);
            }
        }
    }

    /*------------- private method ------------------------------*/

    private void elementCheckOrRemove() {
        if(this.valueMap.size() >= MAX_CAPACITY) {
            lock.lock();
            try {
                this.cleanIfFull();
            } catch (CapacityOutException e) {
                throw e;
            } catch (Exception e) {
                throw new ElementRemoveException(e.getMessage());
            } finally {
                lock.unlock();
            }
        }
    }

    private WhCacheKey createCacheKey(K key, long pExpire) {
        WhCacheKey whCacheKey = new WhCacheKey();
        whCacheKey.key = key;
        whCacheKey.expireTime = System.currentTimeMillis() + pExpire;
        whCacheKey.recentUseTime = System.currentTimeMillis();
        return whCacheKey;
    }

    /*----------------- inner class ----------------*/

    @Getter
    class WhCacheKey {
        private K key;
        private long expireTime;
        private long recentUseTime;
    }
}
