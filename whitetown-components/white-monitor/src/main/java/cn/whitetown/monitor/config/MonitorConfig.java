package cn.whitetown.monitor.config;

import cn.whitetown.monitor.sys.modo.po.ServerInfo;
import cn.whitetown.monitor.sys.modo.po.WhiteOsInfo;
import org.springframework.context.annotation.Configuration;

/**
 * 监控初始化配置
 * @author taixian
 * @date 2020/07/31
 **/
@Configuration
public class MonitorConfig {

    private static WhiteOsInfo osInfo;
    private static ServerInfo serverInfo;

    public synchronized static void setOsInfo(WhiteOsInfo newOsInfo) {
        osInfo = newOsInfo;
    }

    public synchronized static void setServerInfo(ServerInfo newServerInfo) {
        serverInfo = newServerInfo;
    }

    public static WhiteOsInfo getOsInfo() {
        return osInfo;
    }

    public static ServerInfo getServerInfo () {
        return serverInfo;
    }
}
