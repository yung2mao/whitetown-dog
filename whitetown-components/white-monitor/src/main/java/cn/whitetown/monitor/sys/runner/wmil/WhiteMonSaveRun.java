package cn.whitetown.monitor.sys.runner.wmil;

import cn.whitetown.monitor.sys.runner.MonSaveRunnable;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

/**
 * @author taixian
 * @date 2020/08/02
 **/
public class WhiteMonSaveRun implements MonSaveRunnable {
    private MonitorInfoSaveManager monitorInfoSaveManager;
    private WhiteMonitorParams whiteMonitorParams;

    public WhiteMonSaveRun(MonitorInfoSaveManager monitorInfoSaveManager, WhiteMonitorParams whiteMonitorParams) {
        this.monitorInfoSaveManager = monitorInfoSaveManager;
        this.whiteMonitorParams = whiteMonitorParams;
    }

    @Override
    public void run() {
        monitorInfoSaveManager.save(whiteMonitorParams);
    }
}
