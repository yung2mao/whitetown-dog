package cn.whitetown.monitor.sys;

import cn.whitetown.monitor.sys.manager.wiml.WhiteSysMonitorManager;
import org.junit.Test;

/**
 * @author taixian
 * @date 2020/07/31
 **/
public class SysMonitorTest {

    @Test
    public void test01() {
        WhiteSysMonitorManager manager = new WhiteSysMonitorManager();
        System.out.println(manager.getCpuInfo());
    }
}
