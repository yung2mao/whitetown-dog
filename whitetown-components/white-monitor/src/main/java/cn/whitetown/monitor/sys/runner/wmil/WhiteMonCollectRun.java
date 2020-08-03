package cn.whitetown.monitor.sys.runner.wmil;

import cn.whitetown.monitor.sys.runner.SysMonCollectCall;
import cn.whitetown.monitor.sys.manager.SysCollectManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

/**
 * 数据采集线程
 * @author taixian
 * @date 2020/08/02
 **/
public class WhiteMonCollectRun implements SysMonCollectCall<WhiteMonitorParams> {

    private SysCollectManager sysCollectManager;

    public WhiteMonCollectRun(SysCollectManager sysCollectManager) {
        this.sysCollectManager = sysCollectManager;
    }

    @Override
    public WhiteMonitorParams call() throws Exception {
        return sysCollectManager.createMonitorParams();
    }
}
