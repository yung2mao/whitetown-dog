package cn.whitetown.logbase.listen;

import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhClone;

/**
 * 日志监听者管理器
 * @author taixian
 * @date 2020/08/09
 **/
public interface ListenerManager {

    /**
     * 添加一个监听者
     * @param whListener
     */
    void addListener(WhListener whListener);

    /**
     * 通知监听者数据变化
     * @param whPipeline
     * @param <T>
     */
    <T> void eventNotify(WhPipeline<T> whPipeline);

    /**
     * 通知监听者数据变化
     */
    void eventNotify();

    /**
     * 注销监听者
     * @param whListener
     */
    void delListener(WhListener whListener);

    /**
     * 重置日志监听者管理器
     */
    void clearAll();
}
