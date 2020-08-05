package cn.whitetown.monitor.sys.manager.wiml;

import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

import java.io.IOException;
import java.util.Arrays;

/**
 * 多种save manager同时生效
 * @author taixian
 * @date 2020/08/05
 **/
public class White2SaveManager implements MonitorInfoSaveManager {

    private MonitorInfoSaveManager[] monitorInfoSaveManagers;

    public White2SaveManager(MonitorInfoSaveManager ... monitorInfoSaveManagers) {
        this.monitorInfoSaveManagers = monitorInfoSaveManagers;
    }

    @Override
    public void init() throws IOException {
        this.checkSaveManager();
        for(MonitorInfoSaveManager saveManager: monitorInfoSaveManagers) {
            saveManager.init();
        }
    }

    @Override
    public boolean save(WhiteMonitorParams monitorParams) {
        this.checkSaveManager();
        Arrays.stream(monitorInfoSaveManagers).forEach(saveManager -> saveManager.save(monitorParams));
        return true;
    }

    @Override
    public void destroy() {
        this.checkSaveManager();
        Arrays.stream(monitorInfoSaveManagers).forEach(saveManager -> saveManager.destroy());
    }

    private void checkSaveManager() {
        if(monitorInfoSaveManagers == null) {
            throw new NullPointerException("save manager need");
        }
    }
}
