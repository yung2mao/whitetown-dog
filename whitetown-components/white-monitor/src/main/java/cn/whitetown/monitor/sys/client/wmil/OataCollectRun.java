package cn.whitetown.monitor.sys.client.wmil;

import cn.whitetown.monitor.sys.client.SysDataCollectCall;
import cn.whitetown.monitor.sys.manager.SysCollectManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

/**
 * 数据采集线程
 * @author taixian
 * @date 2020/08/02
 **/
public class OataCollectRun implements SysDataCollectCall<WhiteMonitorParams> {

    private SysCollectManager sysCollectManager;

    public OataCollectRun(SysCollectManager sysCollectManager) {
        this.sysCollectManager = sysCollectManager;
    }

    @Override
    public WhiteMonitorParams call() throws Exception {
        return sysCollectManager.createMonitorParams();
    }
}
