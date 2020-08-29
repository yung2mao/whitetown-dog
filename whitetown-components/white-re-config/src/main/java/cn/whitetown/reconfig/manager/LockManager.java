package cn.whitetown.reconfig.manager;

/**
 * redis分布式锁操作
 * @author taixian
 * @date 2020/08/29
 **/
public interface LockManager {

    /**
     * 加锁
     * @param lockName
     * @param lockId
     * @param expireMillis
     * @return
     */
    boolean addLock(String lockName, String lockId, Long expireMillis);

    /**
     * 解锁
     * @param lockName
     * @param lockId
     * @return
     */
    boolean unLock(String lockName, String lockId);

    /**
     * 初始化许可的总数量
     * @param lockName
     * @param totalOrders
     * @param expireSecond
     * @return
     */
    void initPermitNum(String lockName, Long totalOrders, Long expireSecond);

    /**
     * 获取一个订单
     * @param lockName
     * @return
     */
    boolean getOneLock(String lockName);
}
