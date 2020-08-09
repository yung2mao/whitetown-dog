package cn.whitetown.logclient.manager;

import org.apache.log4j.pattern.LogEvent;

/**
 * 客户端日志发布
 * @author taixian
 * @date 2020/08/06
 **/
public interface WhiteLogHandler {

    /**
     * 日志数据发布
     * 处理日志格式化后的字符串
     * @param logName
     * @param logData
     */
    void publish(String logName, String logData);

    /**
     * 日志数据发布 - 直接处理event对象
     * @param logEvent
     */
    void publish(LogEvent logEvent);
}
