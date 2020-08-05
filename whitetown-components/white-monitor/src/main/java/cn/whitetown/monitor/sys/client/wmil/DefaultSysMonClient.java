package cn.whitetown.monitor.sys.client.wmil;

import cn.whitetown.monitor.sys.client.SysMonClient;
import cn.whitetown.monitor.sys.client.SysMonitorRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 默认系统监控客户端
 * @author taixian
 * @date 2020/08/04
 **/
public class DefaultSysMonClient implements SysMonClient {

    @Autowired
    private SysMonitorRunner monitorRunner;

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
    }
}
