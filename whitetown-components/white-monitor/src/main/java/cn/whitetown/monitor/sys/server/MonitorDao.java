package cn.whitetown.monitor.sys.server;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;

import java.util.List;

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
     * @param serverId 服务ID
     * @return
     */
    WhiteMonitorParams getRecent(String serverId);

    /**
     * 获取server对应所有监控数据
     * 控制条数
     * @param serverId
     * @return
     */
    List<WhiteMonitorParams> getAll(String serverId);
}
