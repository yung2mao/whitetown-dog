package cn.whitetown.dogbase.wache;

/**
 * 定义扫描ExpireMap的接口
 * @author taixian
 * @date 2020/05/30
 */
@FunctionalInterface
public interface WhiteExpireMapClean {
    /**
     * 扫描ExpireMap并清除过期数据
     * @param whiteExpireMap
     * @param <K>
     * @param <V>
     */
    <K,V> void scanAndClean(WhiteExpireMap<K,V> whiteExpireMap);
}
