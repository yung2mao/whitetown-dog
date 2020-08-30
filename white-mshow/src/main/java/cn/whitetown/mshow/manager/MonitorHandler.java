package cn.whitetown.mshow.manager;

import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.mshow.modo.ClientSession;

/**
 * 监控信息监听处理
 * @author taixian
 * @date 2020/08/30
 **/
public interface MonitorHandler {

    /**
     * 数据监听
     */
    void listener();

    /**
     * 数据处理
     * @param monitorParams
     */
    void analyzer(WhiteMonitorParams monitorParams);

    /**
     * 数据发送
     * @param session
     */
    void sendMessage(ClientSession session);
}
