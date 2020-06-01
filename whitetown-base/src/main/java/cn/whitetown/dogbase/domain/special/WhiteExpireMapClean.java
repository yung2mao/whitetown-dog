package cn.whitetown.dogbase.domain.special;

public interface WhiteExpireMapClean {
    <K,V> void scanAndClean(WhiteExpireMap<K,V> whiteExpireMap);
}
