package cn.whitetown.logbase.config;

import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.listen.wiml.SimpleListenerManager;
import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logbase.pipe.wiml.WhLogPipeline;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 日志相关常量定义
 * @author taixian
 * @date 2020/08/09
 **/
public class LogConstants {
    /**
     * 默认日志
     */
    public static final String DEFAULT_LOG = "DEFAULT_LOG";
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
    public static final String OP_BASE_LOG = "OP_BASE_LOG";
    /**
     * 接口操作详细日志
     */
    public static final String OP_DETAIL_LOG = "OP_DETAIL_LOG";

    /**
     * 本地存储日志
     */
    public static final String LOCAL_LOG = "LOCAL_LOG";

    public static final String TRACE_ID = "traceId";
    /**
     * 访问日志
     */
    public static final String ACC_LOG = "ACC_LOG";

    public static final Logger SYS_LOGGER = LogManager.getLogger(SYS_LOG);
    public static final Logger DB_LOGGER = LogManager.getLogger(DB_LOG);
    public static final Logger OP_BASE_LOGGER = LogManager.getLogger(OP_BASE_LOG);
    public static final Logger OP_DETAIL_LOGGER = LogManager.getLogger(OP_DETAIL_LOG);
    public static final Logger ACC_LOGGER = LogManager.getLogger(ACC_LOG);
    public static final Logger LOCAL_LOG_LOGGER = LogManager.getLogger(LOCAL_LOG);

    /**
     * 日志中转管道
     */
    public static final WhPipeline<WhLog> LOG_PIPELINE = WhLogPipeline.getDefaultPipeline();
    /**
     * 监听管理器
     */
    public static final ListenerManager LISTENER_MANAGER = SimpleListenerManager.getInstance();

    /**
     * 日志批量存储的数量
     */
    public static final int BATCH_SAVE_LEN = 100;
    /**
     * 内存最大驻留日志数量
     */
    public static final int LOG_CACHE_MAX_LEN = 256;
}
