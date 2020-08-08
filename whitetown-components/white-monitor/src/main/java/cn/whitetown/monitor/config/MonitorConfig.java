package cn.whitetown.monitor.config;

import cn.whitetown.monitor.sys.anno.MonAnnotation;
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
     * 默认 - 本地存储+传输服务端
     * @return
     */
    @Bean(MonAnnotation.CLI_DEFAULT)
    @Lazy
    public SysMonitorRunner monitorRunner() {
        MonitorInfoSaveManager fileSave = WhiteMonFileSaveManager.getInstance();
        MonitorInfoSaveManager sent = WhiteMon2ServerClient.getInstance();
        White2SaveManager white2SaveManager = new White2SaveManager(fileSave, sent);
        SysMonitorRunner monitorRun = new OnceSysMonRun(manager, white2SaveManager);
        SysMonitorRunner monitorRunner = new ScheduleSysMonRun(monitorRun);
        monitorRunner.init(executorService);
        monitorRunner.open();
        return monitorRunner;
    }

    @Bean(MonAnnotation.CLI_FILE_ONLY)
    @Lazy
    public SysMonitorRunner monLocalSave() {
        MonitorInfoSaveManager localSave = WhiteMonFileSaveManager.getInstance();
        OnceSysMonRun onceSysMonRun = new OnceSysMonRun(manager, localSave);
        ScheduleSysMonRun monitorRunner = new ScheduleSysMonRun(onceSysMonRun);
        monitorRunner.init(executorService);
        monitorRunner.open();
        return monitorRunner;
    }

    @Bean(MonAnnotation.CLI_SENT_NOLY)
    @Lazy
    public SysMonitorRunner monSentServer() {
        MonitorInfoSaveManager sent = WhiteMon2ServerClient.getInstance();
        OnceSysMonRun onceSysMonRun = new OnceSysMonRun(manager, sent);
        ScheduleSysMonRun monRunner = new ScheduleSysMonRun(onceSysMonRun);
        monRunner.init(executorService);
        monRunner.open();
        return monRunner;
    }

    /**
     * 系统监控服务端
     * @return
     */
    @Bean
    @Lazy
    public SysMonServer sysMonServer() {
        SysAnalyzerHandler sysAnalyzerHandler = new SysAnalyzerHandler(executorService);
        DefaultSysMonServer sysMonServer = new DefaultSysMonServer(sysAnalyzerHandler);
        sysMonServer.init();
        return sysMonServer;
    }
}
