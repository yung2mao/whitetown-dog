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
    public static final String SYS_LOG = "SYS_LOG";
    /**
     * 数据库操作日志
     */
    public static final String DB_LOG = "DB_LOG";
    /**
     * 接口操作日志
     */
    private static final String OP_LOG = "OP_LOG";
    /**
     * 访问日志
     */
    public static final String ACC_LOG = "ACC_LOG";

    public static final Logger SYS_LOGGER = Logger.getLogger(SYS_LOG);
    public static final Logger DB_LOGGER = Logger.getLogger(DB_LOG);
    public static final Logger OP_LOGGER = Logger.getLogger(OP_LOG);
    public static final Logger ACC_LOGGER = Logger.getLogger(ACC_LOG);

    /**
     * 日志中转管道
     */
    public static final WhPipeline<WhLog> LOG_PIPELINE = WhLogPipeline.getDefaultPipeline();
    /**
     * 监听管理器
     */
    public static final ListenerManager LISTENER_MANAGER = SimpleListenerManager.getInstance();

}
