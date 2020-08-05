package cn.whitetown.monitor.sys.client.wmil;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.sys.client.SysMonitorRunner;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * 定时监控任务
 * @author taixian
 * @date 2020/08/03
 **/
public class ScheduleSysMonRun implements SysMonitorRunner{

    private Logger logger = Logger.getLogger(ScheduleSysMonRun.class);

    private SysMonitorRunner sysMonitorRunner;

    private ScheduledExecutorService schedule;

    public ScheduleSysMonRun(SysMonitorRunner monitorRunner) {
        this.sysMonitorRunner = monitorRunner;
        String poolName = "white-schedule-pool";
        CustomizableThreadFactory threadFactory = new CustomizableThreadFactory();
        threadFactory.setThreadGroupName(poolName);
        schedule = new ScheduledThreadPoolExecutor(1,threadFactory);
    }

    @Override
    public void init(ExecutorService executorService) {
        sysMonitorRunner.init(executorService);
    }

    @Override
    public void run() {
        try {
            schedule.scheduleAtFixedRate(sysMonitorRunner,
                    0,
                    MonConfConstants.SYS_INTERVAL_TIME,
                    TimeUnit.MILLISECONDS);
        }catch (Exception e){
            logger.debug("exception: " +e.getMessage());
        }
    }

    @Override
    public void open() {
        sysMonitorRunner.open();
    }

    @Override
    public void stop() {
        sysMonitorRunner.stop();
    }

    @Override
    public void destroy() {
        try {
            schedule.shutdown();
            boolean wait = schedule.awaitTermination(5, TimeUnit.SECONDS);
            if(!wait) {
                schedule.shutdownNow();
            }
        }catch (Exception e) {
            logger.debug("schedule exception: " + e.getMessage());
        }
        sysMonitorRunner.destroy();
    }
}
