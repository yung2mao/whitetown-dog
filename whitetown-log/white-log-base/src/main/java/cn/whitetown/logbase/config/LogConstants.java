package cn.whitetown.logbase.config;

import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.listen.wiml.SimpleListenerManager;
import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logbase.pipe.wiml.WhLogPipeline;
import org.apache.log4j.Logger;

/**
 * 日志相关常量定义
 * @author taixian
 * @date 2020/08/09
 **/
public class LogConstants {
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

    /**
     * 日志中转
     */
    public static final WhPipeline<WhLog> LOG_PIPELINE = WhLogPipeline.getDefaultPipeline();
    /**
     * 监听管理器
     */
    public static final ListenerManager LISTENER_MANAGER = SimpleListenerManager.getInstance();

}
