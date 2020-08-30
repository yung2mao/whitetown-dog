package cn.whitetown.monitor.sys.server;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

/**
 * 监控信息持久化
 * @author taixian
 * @date 2020/08/30
 **/
public interface MonitorDao {

    /**
     * 数据存储
     * @param whiteMonitorParams
     */
    void save(WhiteMonitorParams whiteMonitorParams);

    /**
     * 读取一条数据
     * @return
     */
    WhiteMonitorParams get();
}
