package cn.whitetown.logclient.manager;

import org.apache.log4j.pattern.LogEvent;

/**
 * @author taixian
 * @date 2020/08/07
 **/
public class SimpleWhiteLogHandler implements WhiteLogHandler{
    
    public WhiteLogHandler whiteLogHandler;
    
    public SimpleWhiteLogHandler(String handlerName) {
        try {
            Class<?> claz = Class.forName(handlerName);
            Object instance = claz.newInstance();
            whiteLogHandler = WhiteLogHandler.class.cast(instance);
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void publish(String logName, String logData) {
        whiteLogHandler.publish(logName,logData);
    }

    @Override
    public void publish(LogEvent logEvent) {
        whiteLogHandler.publish(logEvent);
    }
}
