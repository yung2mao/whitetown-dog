package cn.whitetown.logclient.collect;

import cn.whitetown.logclient.manager.SimpleWhiteLogHandler;
import cn.whitetown.logclient.manager.WhiteLogHandler;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 自定义日志输出
 * @author taixian
 * @date 2020/08/06
 **/
public class WhiteLog4jAppender extends AppenderSkeleton {

    protected String className;

    protected WhiteLogHandler whiteLogHandler;

    @Override
    protected void append(LoggingEvent event) {
        String eventString = "";
        try {
            eventString = this.layout == null ? String.valueOf(event.getMessage()) : this.layout.format(event);
            whiteLogHandler.publish(event.getLoggerName(),eventString);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public WhiteLog4jAppender() {
    }

    public WhiteLog4jAppender(Layout layout) {
        this.layout = layout;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    public void setClassName(String className) {
        this.className = className;
        this.whiteLogHandler = new SimpleWhiteLogHandler(className);
    }
}
