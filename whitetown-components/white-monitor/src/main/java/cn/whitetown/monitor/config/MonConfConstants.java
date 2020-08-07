package cn.whitetown.monitor.config;

import org.apache.log4j.Logger;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 系统监控配置常量
 * @author taixian
 * @date 2020/08/01
 **/
public class MonConfConstants {
    public static final Properties MONITOR_CONF;
    public static final int SYS_INTERVAL_TIME;
    public static final String WORK_ID;
    public static final String SERVER_HOST;
    public static final int SERVER_PORT;
    public static final int WORK_THREAD_SIZE;
    public static final String LOG_SAVE_PATH;
    public static final boolean LOG_SAVE_FAIL_TRY;
    public static final int RETRY_TIMES;
    public static final String PROJECT_DIR = System.getProperty("user.dir");
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final int MON_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE+ 10;

    /*-------日志处理相关--------*/

    public static final String SYS_LOG_NAME = "syslog";
    public static Logger logger = Logger.getLogger(SYS_LOG_NAME);

    /*-----------数据存储相关-------------*/

    public static final String DB_DRIVER_NANE;
    public static final String DB_URL;
    public static final String DB_USERNAME;
    public static final String DB_PASSWORD;
    public static final int DB_TABLE_SHARDING_SIZE;
    public static final long SHARDING_SCOPE = 5000000L;

    static {
        InputStream in = MonitorConfig.class.getClassLoader().getResourceAsStream("white-conf.properties");
        MONITOR_CONF = new Properties();
        try {
            MONITOR_CONF.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SYS_INTERVAL_TIME = Integer.parseInt(MONITOR_CONF.getProperty("sys.monitor.interval.time"));
        WORK_ID = MONITOR_CONF.getProperty("client.id");
        SERVER_HOST = MONITOR_CONF.getProperty("server.host");
        SERVER_PORT = Integer.parseInt(MONITOR_CONF.getProperty("server.port"));
        WORK_THREAD_SIZE = Integer.parseInt(MONITOR_CONF.getProperty("server.work.threadSize"));
        LOG_SAVE_PATH = MONITOR_CONF.getProperty("sys.file.savePath");
        LOG_SAVE_FAIL_TRY = Boolean.parseBoolean(MONITOR_CONF.getProperty("sys.fail.reset.isRetry"));
        RETRY_TIMES = Integer.parseInt(MONITOR_CONF.getProperty("sys.fail.retry.times"));
        DB_DRIVER_NANE = MONITOR_CONF.getProperty("dataSource.driverName");
        DB_URL = MONITOR_CONF.getProperty("dataSource.url");
        DB_USERNAME = MONITOR_CONF.getProperty("dataSource.username");
        DB_PASSWORD = MONITOR_CONF.getProperty("dataSource.password");
        String size = MONITOR_CONF.getProperty("dataSource.table.size");
        DB_TABLE_SHARDING_SIZE = size == null ? 1 : Integer.parseInt(size);
    }
}
