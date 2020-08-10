package cn.whitetown.logclient.collect;

import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logbase.pub.LogPublish;
import cn.whitetown.logclient.manager.SimpleLogPublish;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Date;

/**
 * 自定义日志输出
 * @author taixian
 * @date 2020/08/06
 **/
public class WhiteLog4jAppender extends AppenderSkeleton {

    protected String publishClass;

    protected LogPublish logPublish;

    @Override
    protected void append(LoggingEvent event) {
        if(event == null) {
            return;
        }
        String eventString = "";
        eventString = this.layout == null ? String.valueOf(event.getMessage()) : this.layout.format(event);
        WhLog whLog = new WhLog(event.getLoggerName(),event.getLevel().toInt(),eventString,new Date());
        try {
            logPublish.publish(whLog);
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

    public void setPublishClass(String publishClass) {
        this.publishClass = publishClass;
        this.logPublish = new SimpleLogPublish(publishClass);
    }
}
