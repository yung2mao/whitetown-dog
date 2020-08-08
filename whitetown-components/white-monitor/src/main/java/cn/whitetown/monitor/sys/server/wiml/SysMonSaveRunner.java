package cn.whitetown.monitor.sys.server.wiml;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.monitor.sys.server.wiml.MonScopeSaveManager;

/**
 * 接收数据存储
 * @author taixian
 * @date 2020/08/04
 **/
public class SysMonSaveRunner implements Runnable {

    private static MonScopeSaveManager scopeSaveHandler = new MonScopeSaveManager();

    private WhiteMonitorParams whiteMonitorParams;

    public SysMonSaveRunner(WhiteMonitorParams whiteMonitorParams) {
        this.whiteMonitorParams = whiteMonitorParams;
    }

    @Override
    public void run() {
        scopeSaveHandler.monSave(whiteMonitorParams);
    }
}

