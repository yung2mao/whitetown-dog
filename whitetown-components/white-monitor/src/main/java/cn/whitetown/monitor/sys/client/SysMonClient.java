package cn.whitetown.monitor.sys.client;

/**
 * 系统监控客户端
 * @author taixian
 * @date 2020/08/04
 **/
public interface SysMonClient {

    /**
     * 启动
     * @throws Exception
     */
    void run();

    /**
     * 暂停
     */
    void stop();

    /**
     * 结束
     */
    void destroy();
}
