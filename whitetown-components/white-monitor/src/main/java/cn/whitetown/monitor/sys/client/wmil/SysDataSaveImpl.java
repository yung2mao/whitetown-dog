package cn.whitetown.monitor.sys.client.wmil;

import cn.whitetown.monitor.sys.client.SysDataSave;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

/**
 * @author taixian
 * @date 2020/08/02
 **/
public class SysDataSaveImpl implements SysDataSave {
    private MonitorInfoSaveManager monitorInfoSaveManager;
    private WhiteMonitorParams whiteMonitorParams;

    public SysDataSaveImpl(MonitorInfoSaveManager monitorInfoSaveManager, WhiteMonitorParams whiteMonitorParams) {
        this.monitorInfoSaveManager = monitorInfoSaveManager;
        this.whiteMonitorParams = whiteMonitorParams;
    }

    @Override
    public void run() {
        monitorInfoSaveManager.save(whiteMonitorParams);
    }
}
