package cn.whitetown.monitor.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 常量配置信息
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
    }
}
