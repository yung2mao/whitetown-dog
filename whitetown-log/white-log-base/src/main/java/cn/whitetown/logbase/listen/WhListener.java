package cn.whitetown.logbase.listen;

/**
 * @author taixian
 * @date 2020/08/09
 **/
public interface WhListener {

    /**
     * 向管理器注册为监听者
     * @param listenerManager
     */
    void registry(ListenerManager listenerManager);

    /**
     * 监听数据并处理数据
     * @param t
     */
    <T> void listener(T t);

    /**
     * 从管理器注销
     * @param listenerManager
     */
    void destroy(ListenerManager listenerManager);
}
