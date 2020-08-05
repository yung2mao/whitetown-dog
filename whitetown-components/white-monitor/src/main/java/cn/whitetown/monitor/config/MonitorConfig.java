package cn.whitetown.monitor.config;

import cn.whitetown.monitor.sys.client.SysMonClient;
import cn.whitetown.monitor.sys.client.wmil.DefaultSysMonClient;
import cn.whitetown.monitor.sys.manager.MonitorInfoSaveManager;
import cn.whitetown.monitor.sys.manager.wiml.White2SaveManager;
import cn.whitetown.monitor.sys.manager.wiml.WhiteMonFileSaveManager;
import cn.whitetown.monitor.sys.manager.wiml.WhiteMon2ServerClient;
import cn.whitetown.monitor.sys.manager.wiml.WhiteSysCollectManager;
import cn.whitetown.monitor.sys.client.SysMonitorRunner;
import cn.whitetown.monitor.sys.client.wmil.OnceSysMonRun;
import cn.whitetown.monitor.sys.client.wmil.ScheduleSysMonRun;
import cn.whitetown.monitor.sys.server.DefaultSysMonServer;
import cn.whitetown.monitor.sys.server.SysAnalyzerHandler;
import cn.whitetown.monitor.sys.server.SysMonServer;
import cn.whitetown.monitor.sys.server.SysMonSaveHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

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

    private WhiteSysCollectManager manager = WhiteSysCollectManager.WHITE_SYS_MONITOR;

    /**
     * 初始化系统监控对象
     * 本地保存
     * @return
     */
    @Bean
    @Lazy
    public SysMonitorRunner monitorRunner() {
        MonitorInfoSaveManager fileSave = WhiteMonFileSaveManager.getInstance();
        MonitorInfoSaveManager sent = WhiteMon2ServerClient.getInstance();
        White2SaveManager white2SaveManager = new White2SaveManager(fileSave, sent);
        SysMonitorRunner monitorRun = new OnceSysMonRun(manager, white2SaveManager);
        SysMonitorRunner monitorRunner = new ScheduleSysMonRun(monitorRun);
        monitorRunner.init(executorService);
        monitorRunner.open();
        return monitorRun;
    }

    /**
     * 系统监控客户端
     * @return
     */
    @Bean
    @Lazy
    public SysMonClient sysMonClient() {
        return new DefaultSysMonClient();
    }

    /**
     * 系统监控服务端
     * @return
     */
    @Bean
    @Lazy
    public SysMonServer sysMonServer() {
        SysMonSaveHandler saveHandler = new SysMonSaveHandler();
        SysAnalyzerHandler sysAnalyzerHandler = new SysAnalyzerHandler(executorService, saveHandler);
        DefaultSysMonServer sysMonServer = new DefaultSysMonServer(sysAnalyzerHandler);
        sysMonServer.init();
        return sysMonServer;
    }
}
