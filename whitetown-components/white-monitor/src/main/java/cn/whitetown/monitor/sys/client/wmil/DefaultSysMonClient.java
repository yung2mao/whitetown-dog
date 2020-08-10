package cn.whitetown.monitor.sys.client.wmil;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.monitor.sys.client.SysMonClient;
import cn.whitetown.monitor.sys.client.SysMonitorRunner;
import org.apache.log4j.Logger;

/**
 * 默认系统监控客户端
 * @author taixian
 * @date 2020/08/04
 **/
public class DefaultSysMonClient implements SysMonClient {

    private Logger logger = LogConstants.SYS_LOGGER;

    private static SysMonClient client;

    private SysMonitorRunner monitorRunner;

    private DefaultSysMonClient() {
    }

    public static SysMonClient getInstance(SysMonitorRunner monRunner) {
        if(client == null) {
            DefaultSysMonClient sysMonClient = new DefaultSysMonClient();
            sysMonClient.monitorRunner = monRunner;
            client = sysMonClient;
        }
        return client;
    }

    @Override
    public void run() {
        monitorRunner.run();
    }

    @Override
    public void stop() {
        monitorRunner.stop();
    }

    @Override
    public void destroy() {
        monitorRunner.destroy();
        logger.info("the monitor client is destroy");
    }
}
