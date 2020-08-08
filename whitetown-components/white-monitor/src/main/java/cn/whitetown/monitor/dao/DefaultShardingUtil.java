package cn.whitetown.monitor.dao;

import cn.whitetown.monitor.config.MonConfConstants;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分片规则
 * @author taixian
 * @date 2020/08/07
 **/
public class DefaultShardingUtil implements ShadingUtil{

    public static final DefaultShardingUtil SHARDING_UTIL = new DefaultShardingUtil();

    private static Map<String,List<Field>> shardingKeyMap = new ConcurrentHashMap<>();

    private static final long SCOPE = MonConfConstants.SHARDING_SCOPE;

    private static final int SHARDING_SIZE = MonConfConstants.DB_TABLE_SHARDING_SIZE;

    private DefaultShardingUtil() {}

    /**
     * 范围分片
     * 需在entity中指定分片键
     * @param entity
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public Integer scopeSharding(Object entity) throws IllegalAccessException {
        List<Field> shardingKeys = getShardingKeys(entity);
        if(shardingKeys.size() == 0) {
            return 0;
        }
        return getScopeShardingIndex(shardingKeys,entity);
    }

    /**
     * 哈希取余分片
     * 需在entity中指定分片键
     * @param entity
     * @return
     */
    @Override
    public Integer hashSharding(Object entity) throws IllegalAccessException {
        List<Field> shardingKeys = getShardingKeys(entity);
        if(shardingKeys.size() == 0) {
            return 0;
        }
        return getHashSharding(shardingKeys,entity);
    }

    /**
     * 范围分片
     * 根据key值
     * @param keys
     * @return
     */
    @Override
    public Integer scopeSharding(Object ... keys) {
        long keyTotal = getKeyTotal(keys);
        int index = Long.valueOf(((Long.MAX_VALUE & keyTotal) - 1) / SCOPE).intValue();
        return index >= SHARDING_SIZE ? SHARDING_SIZE - 1 : index;
    }

    /**
     * 哈希取余分片
     * 根据key值
     * @param keys
     * @return
     */
    public Integer hashSharding(Object ... keys) {
        long keyTotal = getKeyTotal(keys);
        return Long.valueOf((Long.MAX_VALUE & keyTotal) % SHARDING_SIZE).intValue();
    }

    private Integer getScopeShardingIndex(List<Field> fields, Object entity) throws IllegalAccessException {
        long total = getKeyTotal(fields,entity);
        int index = Long.valueOf(((Long.MAX_VALUE & total) - 1) / SCOPE).intValue();
        return index >= SHARDING_SIZE ? SHARDING_SIZE - 1 : index;
    }


    private Integer getHashSharding(List<Field> fields, Object entity) throws IllegalAccessException {
        long total = getKeyTotal(fields,entity);
        return Long.valueOf((Long.MAX_VALUE & total) % SHARDING_SIZE).intValue();
    }

    /**
     * 获取分片键Field - List
     * @param entity
     * @return
     */
    private List<Field> getShardingKeys(Object entity) {
        if(entity == null) {
            return new ArrayList<>();
        }
        Class<?> claz = entity.getClass();
        String className = claz.getName();
        List<Field> cacheFields = shardingKeyMap.get(className);
        if(cacheFields != null) {
            return cacheFields;
        }
        Field[] fields = claz.getDeclaredFields();
        List<Field> shardingKeys = new ArrayList<>();
        Arrays.stream(fields).forEach(field -> {
            WhiteTableKey annotation = field.getDeclaredAnnotation(WhiteTableKey.class);
            if(annotation != null) {
                field.setAccessible(true);
                shardingKeys.add(field);
            }
        });
        shardingKeyMap.put(className,shardingKeys);
        return shardingKeys;
    }

    /**
     * 分片键计算总和
     * @param fields
     * @param entity
     * @return
     * @throws IllegalAccessException
     */
    private long getKeyTotal(List<Field> fields, Object entity) throws IllegalAccessException {
        Object[] keys = new Object[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            keys[i] = field.get(entity);
        }
        return getKeyTotal(keys);
    }

    private long getKeyTotal(Object ... keys) {
        if(keys == null || keys.length == 0) {
            return 0;
        }
        long total = 0;
        for(Object key : keys) {
            if(key == null) {
                continue;
            }
            if(key.getClass() == Long.class || key.getClass() == long.class) {
                total += Long.parseLong(String.valueOf(key));
            }else if (key.getClass() == String.class && "\\d+".matches(key.toString())) {
                total += Long.parseLong(key.toString());
            }else {
                total += key.hashCode();
            }
        }
        return total;
    }
}
