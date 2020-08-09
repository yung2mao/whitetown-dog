package cn.whitetown.monitor.sys.client.wmil;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.monitor.sys.client.SysMonitorRunner;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.manager.SysCollectManager;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 监控任务执行
 * init - open - run - stop - destroy
 * @author taixian
 * @date 2020/08/02
 **/
public class OnceSysMonRun implements SysMonitorRunner {

    private Logger logger = LogConstants.sysLogger;

    boolean isActive = false;

    private SysCollectManager collectManager;

    private MonitorInfoSaveManager saveManager;

    private ExecutorService executorService;

    public OnceSysMonRun() {
    }

    public OnceSysMonRun(SysCollectManager collectManager, MonitorInfoSaveManager saveManager) {
        this.collectManager = collectManager;
        this.saveManager = saveManager;
    }

    @Override
    public void init(ExecutorService executorService) {
        this.executorService = executorService;
        try {
            this.saveManager.init();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void run() {
        if(!isActive) {
            return;
        }
        Future<WhiteMonitorParams> future = executorService.submit(new OataCollectRun(collectManager));
        try {
            WhiteMonitorParams whiteMonitorParams = future.get();
            executorService.execute(new SysDataSaveImpl(saveManager, whiteMonitorParams));
        }catch (Exception e) {
            logger.error(e.getMessage());
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
        this.stop();
        saveManager.destroy();
        collectManager = null;
        saveManager = null;
    }
}
