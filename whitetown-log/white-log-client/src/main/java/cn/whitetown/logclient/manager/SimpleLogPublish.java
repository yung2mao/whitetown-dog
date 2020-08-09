package cn.whitetown.logclient.manager;

import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logbase.pub.LogPublish;
import org.apache.log4j.pattern.LogEvent;

/**
 * @author taixian
 * @date 2020/08/07
 **/
public class SimpleLogPublish implements LogPublish {
    
    public LogPublish logPublish;
    
    public SimpleLogPublish(String handlerName) {
        try {
            Class<?> claz = Class.forName(handlerName);
            Object instance = claz.newInstance();
            logPublish = LogPublish.class.cast(instance);
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void init() {
        logPublish.init();
    }

    @Override
    public void publish(WhLog whLog) {
        logPublish.publish(whLog);
    }

    @Override
    public void publish(LogEvent logEvent) {
        logPublish.publish(logEvent);
    }
}
