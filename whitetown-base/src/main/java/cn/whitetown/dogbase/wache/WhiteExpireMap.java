package cn.whitetown.dogbase.wache;

/**
 * 定义具有过期时间的缓存结构
 * @author GrainRain
 * @date 2020/06/21 15:23
 **/
public interface WhiteExpireMap<K,V> extends WhiteCacheBase{
    /**
     * put and don't set expiration time
     * @param key
     * @param value
     * @return
     */
    V put(K key,V value);

    /**
     * put and set expiration time
     * @param key
     * @param value
     * @param pExpire
     * @return
     */
    V put(K key,V value,long pExpire);

    /**
     * put and set expiration time
     * the time unit is second
     * @param key
     * @param value
     * @param expireSecond
     * @return
     */
    V putS(K key,V value,long expireSecond);
    /**
     * set expiration time
     * if there is no such value, then return -1
     * else,return the expiration time
     * @param key
     * @param pExpire
     * @return
     */
    long pExpire(K key,long pExpire);

    /**
     * set expiration time
     * if there is no such value, then return -1
     * else,return the expiration time
     * the time unit is second
     * @param key
     * @param sExpire
     * @return
     */
    long sExpire(K key,long sExpire);

    /**
     * get value
     * @param key
     * @return
     */
    V get(K key);

    /**
     * remove key-value
     * @param key
     * @return
     */
    V remove(K key);

    /**
     * is contains key
     * @param key
     * @return
     */
    boolean containsKey(K key);

    /**
     * 判断key是否已经过期
     * @param key
     * @return
     */
    boolean isExpired(K key);
    /**
     * reset data
     */
    void clean();

    /**
     * if the cache capacity is full,
     * remove some data
     */
    void cleanIfFull();

    /**
     * 扫描具有定期时间的键值对并处理过期的数据
     */
    void expireScan();
}
