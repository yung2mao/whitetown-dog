package cn.whitetown.monitor.sys.server.wiml;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.monitor.sys.server.MonitorDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存存储
 * @author taixian
 * @date 2020/08/30
 **/
public class CacheSaveManager implements MonitorDao {

    private Map<String, List<WhiteMonitorParams>> sysParams;
    private int singleMaxSize = MonConfConstants.SINGLE_MAX_SIZE;

    public CacheSaveManager() {
        this.sysParams = new ConcurrentHashMap<>();
    }

    @Override
    public void save(WhiteMonitorParams whiteMonitorParams) {
        if(whiteMonitorParams == null) { return; }
        String serverId = whiteMonitorParams.getSysBaseInfo().getServerId();
        List<WhiteMonitorParams> list = sysParams.computeIfAbsent(serverId, k -> new ArrayList<>(singleMaxSize));
        if(list.size() > singleMaxSize) {
            list.remove(list.size()-1);
        }
        list.add(whiteMonitorParams);
    }

    @Override
    public WhiteMonitorParams getRecent(String serverId) {
        List<WhiteMonitorParams> list = sysParams.get(serverId);
        if(list == null) { return null; }
        return list.get(0);
    }

    @Override
    public List<WhiteMonitorParams> getAll(String serverId) {
        return sysParams.get(serverId) == null ? new ArrayList<>() : sysParams.get(serverId);
    }
}
