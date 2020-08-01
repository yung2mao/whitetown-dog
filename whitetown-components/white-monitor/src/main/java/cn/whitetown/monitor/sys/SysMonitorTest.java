package cn.whitetown.monitor.sys;

import cn.whitetown.monitor.config.MonitorConfig;
import cn.whitetown.monitor.sys.manager.wiml.WhiteSysMonitorManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author taixian
 * @date 2020/07/31
 **/
public class SysMonitorTest {

    @Test
    public void test01() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
        WhiteSysMonitorManager manager = WhiteSysMonitorManager.WHITE_SYS_MONITOR;
        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                WhiteMonitorParams monitorParams = manager.createMonitorParams();
                System.out.println("执行时间:" + (System.currentTimeMillis() - start));
                System.out.println(monitorParams);
                System.out.println("----------------------------------------------------------------------");
            }
        }, 5,MonitorConfig.SYS_INTERVAL_TIME, TimeUnit.MILLISECONDS);
        countDownLatch.await();
    }
}
