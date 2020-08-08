package cn.whitetown.dogbase.wache.wmil;

import cn.whitetown.dogbase.wache.WhiteExpireMap;

import java.util.Arrays;

/**
 * 具有过期时间的key-value缓存结构 - 多实例结构
 * @author GrainRain
 * @date 2020/06/21 15:22
 **/
public class MultiWhiteExpireMap<K,V> implements WhiteExpireMap<K,V> {
    /**
     * 默认情况下实例个数
     */
    private final static int DEFAULT_CAPACITY = 16;
    /**
     * 最大实例个数
     */
    private final static int MAX_CAPACITY = 128;

    private final int initCapacity;
    /**
     * 用数组维护一个多实例的ExpireMap
     */
    private final SingleWhiteExpireMap<K,V>[] expireMaps;

    /**
     * default情况下，容量为16
     */
    public MultiWhiteExpireMap() {
        this.initCapacity = DEFAULT_CAPACITY;
        expireMaps = new SingleWhiteExpireMap[DEFAULT_CAPACITY];
        this.init();
    }

   /**
     * 指定实例个数
     * @param initialCapacity
     */
    public MultiWhiteExpireMap(int initialCapacity){
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        }
        int n = initialCapacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        initialCapacity =  (n < 0) ? 1 : (n >= MAX_CAPACITY) ? MAX_CAPACITY : n + 1;

        this.initCapacity = initialCapacity;
        expireMaps = new SingleWhiteExpireMap[initialCapacity];
        this.init();
    }


    @Override
    public void init() {
        for (int i = 0; i < expireMaps.length; i++) {
            expireMaps[i] = new SingleWhiteExpireMap<>();
        }
    }

    @Override
    public void destroy() {
        Arrays.stream(expireMaps).forEach(map->map.clean());
    }

    @Override
    public V put(K key, V value) {
        int index = this.getIndex(key);
        return expireMaps[index].put(key,value);
    }

    @Override
    public V put(K key, V value, long pExpire) {
        int index = this.getIndex(key);
        return expireMaps[index].putS(key,value,pExpire);
    }

    @Override
    public V putS(K key, V value, long expireSecond) {
        int index = this.getIndex(key);
        return expireMaps[index].putS(key,value,expireSecond);
    }

    @Override
    public long pExpire(K key, long pExpire) {
        int index = this.getIndex(key);
        return expireMaps[index].pExpire(key,pExpire);
    }

    @Override
    public long sExpire(K key, long sExpire) {
        int index = this.getIndex(key);
        return expireMaps[index].sExpire(key,sExpire);
    }

    @Override
    public V get(K key) {
        int index = this.getIndex(key);
        return expireMaps[index].get(key);
    }

    @Override
    public V remove(K key) {
        int index = this.getIndex(key);
        return expireMaps[index].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        int index = this.getIndex(key);
        return expireMaps[index].containsKey(key);
    }

    @Override
    public boolean isExpired(K key) {
        int index = this.getIndex(key);
        return expireMaps[index].isExpired(key);
    }

    @Override
    public void clean() {
        Arrays.stream(expireMaps).forEach(SingleWhiteExpireMap::clean);
    }

    @Override
    public void cleanIfFull() {
        Arrays.stream(expireMaps).forEach(SingleWhiteExpireMap::cleanIfFull);
    }

    @Override
    public void expireScan() {
        Arrays.stream(expireMaps).forEach(SingleWhiteExpireMap::expireScan);
    }

    /**
     * 获取key值对应数据应当存储的桶index
     * hash取余计算
     * @param key
     * @return
     */
    private int getIndex(K key){
        int hashCode = key.hashCode();
        return hashCode & Integer.MAX_VALUE & (initCapacity-1);
    }
}
