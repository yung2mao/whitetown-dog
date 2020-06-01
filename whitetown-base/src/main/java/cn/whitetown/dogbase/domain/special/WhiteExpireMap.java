package cn.whitetown.dogbase.domain.special;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 具有过期时间的key-value数据结构
 * @author GrainRain
 * @date 2020/05/27 22:13
 **/
@Component
public class WhiteExpireMap<K,V> {
    private ConcurrentHashMap<K,Long> expireKeyMap;
    private ConcurrentHashMap<K,V> valueMap;

    public WhiteExpireMap(){
        this.expireKeyMap = new ConcurrentHashMap<>(16);
        this.valueMap = new ConcurrentHashMap<>(32);
    }

    //put and don't set expiration time
    public V put(K key,V value){
        return valueMap.put(key,value);
    }

    //put and set expiration time
    public V put(K key,V value,long pExpire){
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

    /**
     * set expiration time
     * if there is no such value, then return -1
     * else,return the expiration time
     * @param key
     * @param pExpire
     * @return
     */
    public long pExpire(K key,long pExpire){
        if(pExpire<1){
            return -1;
        }
        V v = valueMap.get(key);
        if(v != null){
            Long expireTime = expireKeyMap.get(key);
            if(expireTime != null){
                if(expireTime > System.currentTimeMillis()){
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

    /**
     * get value
     * @param key
     * @return
     */
    public V get(K key){
        if(expireKeyMap.get(key) != null){
            if(expireKeyMap.get(key) >= System.currentTimeMillis()){
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
    public V remove(K key){
        expireKeyMap.remove(key);
        return valueMap.remove(key);
    }

    /**
     * is contains key
     * @param key
     * @return
     */
    public boolean containsKey(K key){
        return this.get(key) != null;
    }

    /**
     * reset data
     */
    public void clean(){
        expireKeyMap.clear();
        valueMap.clear();
    }

    /**
     * 扫描具有定期时间的键值对并处理过期的数据
     */
    public void expireScan(){
        for(Map.Entry<K,Long> entry: expireKeyMap.entrySet()){
            if (entry.getValue() < System.currentTimeMillis()){
                expireKeyMap.remove(entry.getKey());
                valueMap.remove(entry.getKey());
            }
        }
    }
}
