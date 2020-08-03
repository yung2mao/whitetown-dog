package cn.whitetown.monitor.config;

import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.manager.wiml.WhiteMonFileSaveManager;
import cn.whitetown.monitor.sys.manager.wiml.WhiteSysCollectManager;
import cn.whitetown.monitor.sys.runner.SysMonitorRunner;
import cn.whitetown.monitor.sys.runner.wmil.WhiteOnceSysMonRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;


/**
 * 监控初始化配置
 * @author taixian
 * @date 2020/07/31
 **/
@Configuration
public class MonitorConfig {

    @Autowired
    private ExecutorService executorService;

    /**
     * 初始化系统监控对象
     * @return
     */
    @Bean
    public SysMonitorRunner monitorRunner() {
        WhiteSysCollectManager manager = WhiteSysCollectManager.WHITE_SYS_MONITOR;
        MonitorInfoSaveManager SaveManager = new WhiteMonFileSaveManager();
        SysMonitorRunner monitorRun = new WhiteOnceSysMonRun(manager, SaveManager);
        monitorRun.init(executorService);
        monitorRun.open();
        return monitorRun;
    }
}
