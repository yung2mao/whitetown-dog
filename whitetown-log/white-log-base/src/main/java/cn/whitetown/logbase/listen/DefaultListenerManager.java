package cn.whitetown.logbase.listen;

import java.util.HashSet;
import java.util.Set;

/**
 * 监听管理器实现
 * @author taixian
 * @date 2020/08/09
 **/
public class DefaultListenerManager<T> implements ListenerManager {

    private Set<WhListener> listeners;

    public DefaultListenerManager() {
        this.listeners = new HashSet<>();
    }

    @Override
    public void addListener(WhListener whListener) {
        if (whListener != null) {
            listeners.remove(whListener);
            listeners.add(whListener);
        }
    }

    @Override
    public <T> void notifyAll(T t) {

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
