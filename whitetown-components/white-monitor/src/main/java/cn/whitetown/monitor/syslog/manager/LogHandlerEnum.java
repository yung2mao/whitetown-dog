package cn.whitetown.monitor.syslog.manager;

import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.monitor.syslog.manager.wiml.DataBaseLogHandler;

/**
 * @author taixian
 * @date 2020/08/06
 **/
public enum LogHandlerEnum {

    /**
     * 数据库存储
     */
    LOG_DB_HANDLER(MonConfConstants.LOG_DB_HANDLER,new DataBaseLogHandler());

    private String name;
    private WhiteLogHandler whiteLogHandler;

    LogHandlerEnum(String name, WhiteLogHandler whiteLogHandler) {
        this.name = name;
        this.whiteLogHandler = whiteLogHandler;
    }

    public static WhiteLogHandler getLogHandler(String type) {
        LogHandlerEnum logHandlerEnum = valueOf(type);
        return logHandlerEnum == null ? null : logHandlerEnum.whiteLogHandler;
    }
}
