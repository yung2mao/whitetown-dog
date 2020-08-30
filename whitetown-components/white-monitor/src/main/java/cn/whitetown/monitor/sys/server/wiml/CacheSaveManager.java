package cn.whitetown.monitor.sys.server.wiml;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.monitor.sys.server.MonitorDao;
import cn.whitetown.monitor.util.DestroyHook;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 内存存储
 * @author taixian
 * @date 2020/08/30
 **/
public class CacheSaveManager implements MonitorDao {

    private ConcurrentLinkedQueue<WhiteMonitorParams> paramsQueue;

    private Integer maxSize = MonConfConstants.CACHE_MAX_SIZE;

    public CacheSaveManager() {
        this.paramsQueue = new ConcurrentLinkedQueue<>();
        DestroyHook.destroy(()->{
            paramsQueue.clear();
            paramsQueue = null;
        });
    }

    @Override
    public void save(WhiteMonitorParams whiteMonitorParams) {
        synchronized (this) {
            paramsQueue.offer(whiteMonitorParams);
            if(paramsQueue.size() > maxSize) {
                paramsQueue.poll();
            }
        }
    }

    @Override
    public WhiteMonitorParams get() {
        return paramsQueue.poll();
    }

}
