package cn.whitetown.logclient.manager.wiml;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logbase.pub.LogPublish;
import org.apache.log4j.Logger;
import org.apache.log4j.pattern.LogEvent;

import java.util.concurrent.*;

/**
 * 日志投递作业
 * @author taixian
 * @date 2020/08/06
 **/
public class WhLogPublish implements LogPublish {

    private Logger logger;

    private ExecutorService threadPool;

    private ListenerManager listenerManager;

    private WhPipeline<WhLog> logPipeline;

    private boolean isInit = false;

    public WhLogPublish() {
        int core = 0;
        int temp = 10;
        long keepActive = 60;
        int queueSize = 1024;
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(core, temp, keepActive, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize), threadFactory);
    }

    @Override
    public void init() {
        this.logger = LogConstants.sysLogger;
        this.listenerManager = LogConstants.LISTENER_MANAGER;
        this.logPipeline = LogConstants.LOG_PIPELINE;
        if(logger == null || listenerManager == null || logPipeline == null) {
            return;
        }
        isInit = true;
    }

    @Override
    public void publish(WhLog whLog){
        if(!isInit) {
            this.init();
        }
        boolean isAdd = logPipeline.addElement(whLog);
        if(!isAdd) {
            logger.debug("add error, current size is " + logPipeline.size() + ", max size is "+logPipeline.maxSize());
        }
        threadPool.execute(() -> listenerManager.eventNotify());
    }

    @Override
    public void publish(LogEvent logEvent) {

    }
}
