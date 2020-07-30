package cn.whitetown.dogbase.common.memdata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 具有过期时间的key-value缓存结构 - 单实例结构
 * @author GrainRain
 * @date 2020/05/27 22:13
 **/
public class SingleWhiteExpireMap<K,V> implements WhiteExpireMap<K,V>{
    private ConcurrentHashMap<K,Long> expireKeyMap;
    private ConcurrentHashMap<K,V> valueMap;

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
        return valueMap.put(key,value);
    }

    @Override
    public V put(K key, V value, long pExpire){
        if(pExpire<1){
            return value;
        }
        try {
            expireKeyMap.put(key,System.currentTimeMillis()+pExpire);
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
        return this.put(key,value,1000 * expireSecond);
    }

    @Override
    public long pExpire(K key, long pExpire){
        if(pExpire<1){
            return -1;
        }
        V v = valueMap.get(key);
        if(v != null){
            Long expireTime = expireKeyMap.get(key);
            if(expireTime != null){
                if(!this.isExpired(key)){
                    expireKeyMap.put(key,System.currentTimeMillis()+pExpire);
                    return System.currentTimeMillis()+pExpire;
                }else {
                    return -1;
                }
            }else {
                expireKeyMap.put(key,System.currentTimeMillis()+pExpire);
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
        if(expireKeyMap.get(key) != null){
            if(!this.isExpired(key)){
                return valueMap.get(key);
            }else {
                expireKeyMap.remove(key);
                valueMap.remove(key);
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
        if(expireKeyMap.get(key) < System.currentTimeMillis()){
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
}
