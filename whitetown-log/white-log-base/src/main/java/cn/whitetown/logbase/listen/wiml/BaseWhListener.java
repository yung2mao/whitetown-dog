package cn.whitetown.logbase.listen.wiml;

import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.listen.WhListener;
import cn.whitetown.logbase.pipe.WhPipeline;

/**
 * 日志服务端处理默认实现
 * @author taixian
 * @date 2020/08/09
 **/
public abstract class BaseWhListener<T> implements WhListener<T> {

    protected ListenerManager listenerManager;

    @Override
    public void registry(ListenerManager listenerManager) {
        listenerManager.addListener(this);
        this.listenerManager = listenerManager;

    }

    @Override
    public void destroy(ListenerManager listenerManager) {
        listenerManager.delListener(this);
    }
}
