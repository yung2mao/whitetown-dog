package cn.whitetown.monitor.sys.server;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

/**
 * 监控数据存储管理
 * @author taixian
 * @date 2020/08/08
 **/
public interface MonSaveRunner {

    /**
     * 监控数据服务端存储
     * @param whiteMonitorParams
     */
    void monSave(WhiteMonitorParams whiteMonitorParams);
}
