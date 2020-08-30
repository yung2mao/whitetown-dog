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
    boolean lock(String lockName, String lockId, Long expireMillis);

    /**
     * 解锁
     * @param lockName
     * @param lockId
     * @return
     */
    boolean unLock(String lockName, String lockId);

    /**
     * 初始化库存信息
     * @param productName
     * @param totalInStock
     * @param expireSecond
     * @return
     */
    void initInStock(String productName, Long totalInStock, Long expireSecond);

    /**
     * 减少一个库存
     * @param productName
     * @return
     */
    boolean decrementInStock(String productName);

    /**
     * 归还一个库存
     * @param productName
     * @return
     */
    Long returnInStock(String productName);
}
