package cn.whitetown.logbase.listen.wiml;

import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.listen.WhListener;
import cn.whitetown.logbase.pipe.WhPipeline;

import java.util.HashSet;
import java.util.Set;

/**
 * 监听管理器实现
 * @author taixian
 * @date 2020/08/09
 **/
public class SimpleListenerManager implements ListenerManager {

    protected Set<WhListener> listeners;

    private SimpleListenerManager() {
        this.listeners = new HashSet<>();
    }

    private SimpleListenerManager(Set<WhListener> listeners) {
        this.listeners = listeners;
    };

    public static ListenerManager getInstance() {
        return new SimpleListenerManager();
    }

    public static ListenerManager getInstance(Set<WhListener> listeners) {
        return new SimpleListenerManager(listeners);
    }

    @Override
    public void addListener(WhListener whListener) {
        if (whListener != null) {
            listeners.remove(whListener);
            listeners.add(whListener);
        }
    }

    @Override
    public <T> void eventNotify(WhPipeline<T> whPipeline) {
        if(whPipeline == null) {
            return;
        }
        listeners.forEach(listener->listener.listener(this,whPipeline));
    }

    @Override
    public void eventNotify() {
        listeners.forEach(WhListener::listener);
    }

    @Override
    public void delListener(WhListener whListener) {
        if(whListener != null) {
            listeners.remove(whListener);
        }
    }

    @Override
    public void clearAll() {
        listeners.clear();
    }
}
