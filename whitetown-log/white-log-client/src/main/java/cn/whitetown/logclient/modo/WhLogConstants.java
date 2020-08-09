package cn.whitetown.logclient.modo;

import org.apache.log4j.Logger;

/**
 * 日志相关常量定义
 * @author taixian
 * @date 2020/08/09
 **/
public class WhLogConstants {
    /**
     * 系统基本日志
     */
    public static final String SYS_LOG_NAME = "sysLog";
    /**
     * 数据库操作日志
     */
    public static final String DB_LOG_NAME = "dbLog";
    /**
     * 接口操作日志
     */
    private static final String OP_LOG_NAME = "opLog";
    /**
     * 访问日志
     */
    public static final String ACC_LOG_NAME = "accLog";

    public static Logger sysLogger = Logger.getLogger(SYS_LOG_NAME);
    public static Logger dbLogger = Logger.getLogger(DB_LOG_NAME);
    public static Logger opLogger = Logger.getLogger(OP_LOG_NAME);
    public static Logger accLogger = Logger.getLogger(ACC_LOG_NAME);
}
