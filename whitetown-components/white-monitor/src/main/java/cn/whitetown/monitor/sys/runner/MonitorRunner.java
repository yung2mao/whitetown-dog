package cn.whitetown.monitor.sys.runner;

import java.util.concurrent.ExecutorService;

/**
 * 系统信息监控启动入口
 * @author taixian
 * @date 2020/08/02
 **/
public interface MonitorRunner {

    /**
     * 初始化方法
     * @param executorService 线程池
     */
    void init(ExecutorService executorService);

    /**
     * 执行系统监控
     */
    void run();

    /**
     * 允许执行
     * @return
     */
    void open();

    /**
     * 停止监控方法
     * @return 结束成功与否
     */
    void stop();

    /**
     * 销毁时调用
     */
    void destroy();
}
