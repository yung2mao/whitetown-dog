package cn.whitetown.mshow.manager;

/**
 * 监控数据发送管理器
 * @author taixian
 * @date 2020/08/30
 **/
public interface MonitorSendManager {

    /**
     * 数据发送到所有连接的客户端
     */
    void sendAllClient();
}
