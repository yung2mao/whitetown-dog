package cn.whitetown.monitor.sys.server;

/**
 * 监控服务端
 * @author taixian
 * @date 2020/08/04
 **/
public interface SysMonServer {
    /**
     * 初始化方法
     */
    void init();

    /**
     * 执行
     */
    void run();

    /**
     * 销毁
     */
    void destroy();
}
