package cn.whitetown.monitor.syslog.collect;

import cn.whitetown.monitor.syslog.manager.LogHandlerEnum;
import cn.whitetown.monitor.syslog.manager.WhiteLogHandler;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 自定义日志输出
 * @author taixian
 * @date 2020/08/06
 **/
public class WhiteLog4jAppender extends AppenderSkeleton {

    protected String handlerType;

    protected WhiteLogHandler whiteLogHandler;

    @Override
    protected void append(LoggingEvent event) {
        String eventString = "";
        try {
            eventString = this.layout == null ? String.valueOf(event.getMessage()) : this.layout.format(event);
            whiteLogHandler.analyzer(event.getLoggerName(),eventString);
        }catch (Exception e) {
            e.printStackTrace();
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


    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
        this.whiteLogHandler = LogHandlerEnum.getLogHandler(handlerType);
    }


}