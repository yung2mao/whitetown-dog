package cn.whitetown.monitor.sys.server;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import org.apache.log4j.Logger;

/**
 * 接收数据存储
 * @author taixian
 * @date 2020/08/04
 **/
public class SysMonSaveHandler implements Runnable {

    private static WhMonScopeSaveHandler scopeSaveHandler = new WhMonScopeSaveHandler();

    private WhiteMonitorParams whiteMonitorParams;

    public SysMonSaveHandler(WhiteMonitorParams whiteMonitorParams) {
        this.whiteMonitorParams = whiteMonitorParams;
    }

    @Override
    public void run() {
        scopeSaveHandler.monSave(whiteMonitorParams);
    }
}

