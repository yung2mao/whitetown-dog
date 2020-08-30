package cn.whitetown.mshow.config;

import cn.whitetown.monitor.sys.anno.WhiteMonClient;
import cn.whitetown.monitor.sys.anno.WhiteMonServer;
import cn.whitetown.monitor.sys.server.MonitorDao;
import cn.whitetown.monitor.sys.server.config.MonServerConfig;
import cn.whitetown.monitor.sys.server.wiml.CacheSaveManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author taixian
 * @date 2020/08/30
 **/
@WhiteMonServer
@WhiteMonClient
@Component
public class SysMonitorConfig {

    public static final String GROUP_PREFIX = "sys_monitor";

    @Bean
    @Lazy
    public CacheSaveManager cacheSaveManager(){
        List<MonitorDao> monitorDaos = MonServerConfig.CONFIG.getMonitorDao();
        for(MonitorDao monDao : monitorDaos) {
            if(monDao.getClass() == CacheSaveManager.class) {
                return (CacheSaveManager) monDao;
            }
        }
        System.err.println("can not find monitor cache class");
        System.exit(0);
        return null;
    }
}
