package cn.whitetown.logclient.manager.wiml;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logbase.pub.LogPublish;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;

import java.util.Date;

/**
 * 日志投递作业
 * @author taixian
 * @date 2020/08/06
 **/
public class WhLogPublish implements LogPublish {

    private ListenerManager listenerManager;

    private WhPipeline<WhLog> logPipeline;

    private boolean isInit = false;

    public WhLogPublish() {
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
        if(whLog.getLogLevel() == Level.DEBUG.intLevel()) {
            System.out.println(whLog);
            return;
        }
        boolean isAdd = logPipeline.put(whLog);
        if(!isAdd) {
            System.err.println("add error, current size is " + logPipeline.size() + ", max size is "+logPipeline.maxSize());
        }
        listenerManager.eventNotify();
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
        whLog.setLogLevel(logEvent.getLevel().intLevel());
        whLog.setLogData(String.valueOf(logEvent.getMessage()));
        whLog.setTimeStamp(new Date());
        this.publish(whLog);
    }
}
