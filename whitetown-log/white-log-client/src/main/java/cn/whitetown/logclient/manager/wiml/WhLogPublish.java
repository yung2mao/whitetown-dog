package cn.whitetown.logclient.manager.wiml;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logbase.pub.LogPublish;
import cn.whitetown.logclient.manager.WhSimpleThreadPoolFactory;
import org.apache.log4j.Level;
import org.apache.log4j.pattern.LogEvent;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 日志投递作业
 * @author taixian
 * @date 2020/08/06
 **/
public class WhLogPublish implements LogPublish {

    private ExecutorService threadPool;

    private ListenerManager listenerManager;

    private WhPipeline<WhLog> logPipeline;

    private boolean isInit = false;

    public WhLogPublish() {
        int core = 0;
        int total = 60;
        long keepActive = 60;
        int queueSize = 1;
        ThreadFactory threadFactory = new WhSimpleThreadPoolFactory("log-pool");
        threadPool = new ThreadPoolExecutor(core, total, keepActive, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize), threadFactory);
        this.init();
    }

    @Override
    public void init() {
        this.listenerManager = LogConstants.LISTENER_MANAGER;
        this.logPipeline = LogConstants.LOG_PIPELINE;
        //why !!! 常量竟然为null
        if(this.listenerManager != null && this.logPipeline != null) {
            isInit = true;
        }
    }

    @Override
    public void publish(WhLog whLog){
        if(!isInit) {
            this.init();
        }
        if(whLog.getLogLevel() == Level.DEBUG_INT) {
            System.out.println(whLog);
            return;
        }
        boolean isAdd = logPipeline.put(whLog);
        if(!isAdd) {
            System.err.println("add error, current size is " + logPipeline.size() + ", max size is "+logPipeline.maxSize());
        }
        threadPool.submit(() -> listenerManager.eventNotify());
    }

    @Override
    public void publish(LogEvent logEvent) {
        Level level = logEvent.getLevel();
        if(level.equals(Level.DEBUG)) {
            System.out.println(logEvent);
            return;
        }
        WhLog whLog = new WhLog();
        whLog.setLogName(logEvent.getLoggerName());
        whLog.setLogLevel(logEvent.getLevel().toInt());
        whLog.setLogData(String.valueOf(logEvent.getMessage()));
        whLog.setTimeStamp(new Date());
        this.publish(whLog);
    }
}
