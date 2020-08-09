package cn.whitetown.logbase.listen;

import cn.whitetown.logbase.pipe.WhPipeline;

/**
 * @author taixian
 * @date 2020/08/09
 **/
public interface WhListener<T>{

    /**
     * 向管理器注册为监听者
     * @param listenerManager
     */
    void registry(ListenerManager listenerManager);

    /**
     * 数据更新调用
     * @param listenerManager
     * @param whPipeline
     */
    void listener(ListenerManager listenerManager, WhPipeline<T> whPipeline);

    /**
     * 管道数据变化操作
     */
    void listener();

    /**
     * 从管理器注销
     * @param listenerManager
     */
    void destroy(ListenerManager listenerManager);
}
