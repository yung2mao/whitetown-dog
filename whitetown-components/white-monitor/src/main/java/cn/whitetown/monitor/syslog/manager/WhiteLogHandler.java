package cn.whitetown.monitor.syslog.manager;

import org.apache.log4j.pattern.LogEvent;

/**
 * @author taixian
 * @date 2020/08/06
 **/
public interface WhiteLogHandler {

    /**
     * 日志数据处理
     * 处理日志格式化后的字符串
     * @param logName
     * @param logData
     */
    void analyzer(String logName, String logData);

    /**
     * 日志数据处理 - 直接处理event对象
     * @param logEvent
     */
    void analyzer(LogEvent logEvent);
}
