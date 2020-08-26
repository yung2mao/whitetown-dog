package cn.whitetown.mshow.manager.wiml;

import cn.whitetown.dogbase.wache.WhiteExpireMap;
import cn.whitetown.mshow.config.WebSocketConfig;
import cn.whitetown.mshow.manager.SocketCache;

/**
 * websocket缓存实现
 * @author taixian
 * @date 2020/08/25
 **/
public class DefaultSocketCache implements SocketCache {

    private WhiteExpireMap expireMap;

    public DefaultSocketCache(WhiteExpireMap expireMap) {
        this.expireMap = expireMap;
    }

    @Override
    public void saveConnectUser(String randomId, Long userId) {
        expireMap.putS(randomId,userId, WebSocketConfig.CONNECT_PARAM_CACHE_TIME);
    }

    @Override
    public Long getUserId(String randomId) {
        Object userId = expireMap.remove(randomId);
        if(userId == null) {
            return null;
        }
        return Long.parseLong(String.valueOf(userId));
    }
}
