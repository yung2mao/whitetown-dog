package cn.whitetown.logbase.listen;

/**
 * 日志服务端处理默认实现
 * @author taixian
 * @date 2020/08/09
 **/
public class DefaultListener implements WhListener {

    private ListenerManager listenerManager;

    @Override
    public void registry(ListenerManager listenerManager) {
        listenerManager.addListener(this);

    }

    @Override
    public <T> void listener(T t) {

    }

    @Override
    public void destroy(ListenerManager listenerManager) {

    }
}
