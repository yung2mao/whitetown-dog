package cn.whitetown.monitor.sys;

import cn.whitetown.monitor.config.MonitorConfConstants;
import cn.whitetown.monitor.sys.runner.SysMonitorRunner;
import cn.whitetown.monitor.sys.runner.wmil.WhiteOnceSysMonRun;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.manager.wiml.WhiteSysCollectManager;
import cn.whitetown.monitor.sys.manager.wiml.WhiteMonFileSaveManager;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author taixian
 * @date 2020/07/31
 **/
public class SysMonitorTest {

    @Test
    public void test01() throws InterruptedException {
/*        CountDownLatch countDownLatch = new CountDownLatch(1);
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
        WhiteSysCollectManager manager = WhiteSysCollectManager.WHITE_SYS_MONITOR;
        MonitorInfoSaveManager SaveManager = new WhiteMonFileSaveManager();
        SysMonitorRunner monitorRun = new WhiteOnceSysMonRun(manager, SaveManager);
        monitorRun.init(Executors.newFixedThreadPool(2));
        monitorRun.open();
        schedule.scheduleAtFixedRate(() -> monitorRun.run(), 5, MonitorConfConstants.SYS_INTERVAL_TIME, TimeUnit.MILLISECONDS);
        countDownLatch.await();*/
    }

    @Test
    public void test02() throws IOException {
        WhiteMonFileSaveManager whiteSysFileManager = new WhiteMonFileSaveManager();
        whiteSysFileManager.init();
    }
}
