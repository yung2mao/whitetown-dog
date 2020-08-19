package cn.whitetown.logbase.pub;

import cn.whitetown.logbase.pipe.modo.WhLog;
import org.apache.logging.log4j.core.LogEvent;

/**
 * 日志投递接口定义
 * @author taixian
 * @date 2020/08/09
 **/
public interface LogPublish {

    /**
     * 初始化
     */
    void init();

    /**
     * 日志处理后数据投递
     * @param whLog
     * @return
     */
    void publish(WhLog whLog);

    /**
     * 日志事件直接投递
     * @param event
     */
    void publish(LogEvent event);
}
