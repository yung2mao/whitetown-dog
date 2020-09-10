package cn.whitetown.logserver.manager.define;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认日志处理
 * @author taixian
 * @date 2020/08/10
 **/
public class DefaultLogAnalyzer implements WhLogAnalyzer {

    private Logger logger = LogConstants.LOCAL_LOG_LOGGER;
    /**
     * 解析处理器是否正常
     */
    private boolean status = true;
    /**
     * 失败恢复时间
     */
    private long restoreTime = 60000;
    /**
     * 重试次数
     */
    private final int retryTimes = 5;
    private AtomicInteger reTimes = new AtomicInteger(retryTimes);
    /**
     * 前次失败时间
     */
    private long lastFailTime = 0;

    @Override
    public void analyzer(WhLog whLog) {
        if (whLog.getLogLevel() < Level.ERROR.intLevel()) {
            logger.warn(whLog);
            return;
        }
        System.err.println(whLog);
    }

    @Override
    public void save() {
    }

    @Override
    public boolean status() {
        if(status) {
            return true;
        }
        long nowTime = System.currentTimeMillis();
        if((nowTime - lastFailTime) < restoreTime) {
            return false;
        }
        synchronized (this) {
            restoreTime += 1000;
            if(nowTime - lastFailTime > restoreTime) {
                reTimes.set(retryTimes);
                lastFailTime = nowTime;
                status = true;
                return true;
            }
            return false;
        }
    }

    @Override
    public void errorHandle(WhLog whLog, Exception ex) {
        logger.warn(whLog);
        if(ex == null) {
            return;
        }
        logger.error(ex.getMessage());
        if(ex instanceof IOException) {
            if(reTimes.decrementAndGet() < 1) {
                status = false;
                lastFailTime = System.currentTimeMillis();
                return;
            }
            return;
        }
        throw new IllegalArgumentException(ex.getMessage());
    }

    @Override
    public void destroy() {
    }
}
