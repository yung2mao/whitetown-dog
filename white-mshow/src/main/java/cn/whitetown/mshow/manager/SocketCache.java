package cn.whitetown.mshow.manager;

/**
 * websocket用户session映射关系缓存
 * @author taixian
 * @date 2020/08/25
 **/
public interface SocketCache {

    /**
     * 临时存储解析获取的userId
     * @param randomId
     * @param userId
     */
    void saveConnectUser(String randomId, Long userId);

    /**
     * 获取并移除解析得到的user信息
     * @param randomId
     * @return
     */
    public Long getUserId(String randomId);
}
