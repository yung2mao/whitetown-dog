package cn.whitetown.monitor.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 常量配置信息
 * @author taixian
 * @date 2020/08/01
 **/
public class MonitorConfConstants {
    public static final Properties MONITOR_CONF;

    public static final int SYS_INTERVAL_TIME;
    public static final String SERVER_ID;
    public static final String LOG_SAVE_PATH;
    public static final boolean LOG_SAVE_FAIL_TRY;
    public static final int RETRY_TIMES;
    public static final String PROJECT_DIR = System.getProperty("user.dir");

    static {
        InputStream in = MonitorConfig.class.getClassLoader().getResourceAsStream("white-conf.properties");
        MONITOR_CONF = new Properties();
        try {
            MONITOR_CONF.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SYS_INTERVAL_TIME = Integer.parseInt(MONITOR_CONF.getProperty("sys.monitor.interval.time"));
        SERVER_ID = MONITOR_CONF.getProperty("server.id");
        LOG_SAVE_PATH = MONITOR_CONF.getProperty("sys.file.savePath");
        LOG_SAVE_FAIL_TRY = Boolean.parseBoolean(MONITOR_CONF.getProperty("sys.fail.reset.isRetry"));
        RETRY_TIMES = Integer.parseInt(MONITOR_CONF.getProperty("sys.fail.retry.times"));
    }
}
