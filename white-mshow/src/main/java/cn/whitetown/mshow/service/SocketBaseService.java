package cn.whitetown.mshow.service;

/**
 * socket基本操作服务
 * @author taixian
 * @date 2020/08/31
 **/
public interface SocketBaseService {

    /**
     * session组变更
     * @param userId
     * @param groupId
     */
    void groupChange(Long userId, String groupId);
}
