package cn.whitetown.monitor.sys;

import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.manager.wiml.WhiteMonFileSaveManager;
import cn.whitetown.monitor.sys.manager.wiml.WhiteSysCollectManager;
import cn.whitetown.monitor.sys.runner.SysMonitorRunner;
import cn.whitetown.monitor.sys.runner.wmil.WhiteOnceSysMonRun;
import cn.whitetown.monitor.sys.runner.wmil.WhiteScheduleSysMonRun;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * @author taixian
 * @date 2020/07/31
 **/
public class SysMonitorTest {

    @Test
    public void test01() throws InterruptedException {
        WhiteSysCollectManager manager = WhiteSysCollectManager.WHITE_SYS_MONITOR;
        MonitorInfoSaveManager SaveManager = new WhiteMonFileSaveManager();
        SysMonitorRunner monitorRun = new WhiteOnceSysMonRun(manager, SaveManager);
        SysMonitorRunner monitorRunner = new WhiteScheduleSysMonRun(monitorRun);
        monitorRunner.init(Executors.newFixedThreadPool(2));
        monitorRunner.open();
        monitorRunner.run();
        Thread.sleep(60000);
        monitorRunner.stop();
        monitorRunner.destroy();
    }

    @Test
    public void test02() throws IOException {
        WhiteMonFileSaveManager whiteSysFileManager = new WhiteMonFileSaveManager();
        whiteSysFileManager.init();
    }
}
