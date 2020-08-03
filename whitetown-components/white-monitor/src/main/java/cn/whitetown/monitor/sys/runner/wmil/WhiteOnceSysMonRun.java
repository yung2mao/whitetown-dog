package cn.whitetown.monitor.sys.runner.wmil;

import cn.whitetown.monitor.sys.runner.SysMonitorRunner;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.manager.SysCollectManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 监控任务执行
 * int - open - run - stop - destroy
 * @author taixian
 * @date 2020/08/02
 **/
public class WhiteOnceSysMonRun implements SysMonitorRunner {

    private Logger logger = Logger.getLogger(WhiteOnceSysMonRun.class);

    boolean isActive = false;

    private SysCollectManager collectManager;

    private MonitorInfoSaveManager saveManager;

    private ExecutorService executorService;

    public WhiteOnceSysMonRun() {
    }

    public WhiteOnceSysMonRun(SysCollectManager collectManager, MonitorInfoSaveManager saveManager) {
        this.collectManager = collectManager;
        this.saveManager = saveManager;
    }

    @Override
    public void init(ExecutorService executorService) {
        this.executorService = executorService;
        try {
            this.saveManager.init();
        } catch (IOException e) {
            logger.warn("exception: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        if(!isActive) {
            return;
        }
        Future<WhiteMonitorParams> future = executorService.submit(new WhiteMonCollectRun(collectManager));
        try {
            WhiteMonitorParams whiteMonitorParams = future.get();
            executorService.execute(new WhiteSysMonSaveRun(saveManager,whiteMonitorParams));
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("exception: " + e.getMessage());
        }
    }

    @Override
    public void open() {
        this.isActive = true;
    }

    @Override
    public void stop() {
        this.isActive = false;
    }

    @Override
    public void destroy() {
        saveManager.destroy();
        collectManager = null;
        saveManager = null;
    }
}
