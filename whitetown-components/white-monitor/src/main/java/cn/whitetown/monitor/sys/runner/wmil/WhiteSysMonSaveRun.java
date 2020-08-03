package cn.whitetown.monitor.sys.runner.wmil;

import cn.whitetown.monitor.sys.runner.SysMonSaveRunnable;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

/**
 * @author taixian
 * @date 2020/08/02
 **/
public class WhiteSysMonSaveRun implements SysMonSaveRunnable {
    private MonitorInfoSaveManager monitorInfoSaveManager;
    private WhiteMonitorParams whiteMonitorParams;

    public WhiteSysMonSaveRun(MonitorInfoSaveManager monitorInfoSaveManager, WhiteMonitorParams whiteMonitorParams) {
        this.monitorInfoSaveManager = monitorInfoSaveManager;
        this.whiteMonitorParams = whiteMonitorParams;
    }

    @Override
    public void run() {
        monitorInfoSaveManager.save(whiteMonitorParams);
    }
}
