package cn.whitetown.logclient.collect;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logbase.pub.LogPublish;
import cn.whitetown.logclient.manager.SimpleLogPublish;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.Date;

/**
 * 自定义日志输出
 * @author taixian
 * @date 2020/08/06
 **/
@Plugin(
        name = "WhiteLog4jAppender",
        category = "Core",
        elementType = "appender",
        printObject = true
)
public class WhiteLog4jAppender extends AbstractAppender {

    protected String publishClass;

    protected LogPublish logPublish;

    protected WhiteLog4jAppender(String name, Filter filter, Layout<? extends Serializable> layout,String publishClass) {
        super(name, filter, layout,true, Property.EMPTY_ARRAY);
        this.setPublishClass(publishClass);

    }

    public void setPublishClass(String publishClass) {
        this.publishClass = publishClass;
        this.logPublish = new SimpleLogPublish(publishClass);
    }

    @Override
    public void append(LogEvent event) {
        if(event == null) {
            return;
        }
        String eventString = String.valueOf(super.getLayout().toSerializable(event));
        eventString = StrUtil.removeSuffix(eventString,"\n");
        WhLog whLog = new WhLog(event.getLoggerName(),event.getLevel().intLevel(),eventString,new Date());
        try {
            ThreadUtil.execAsync(()->logPublish.publish(whLog));
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @PluginFactory
    public static WhiteLog4jAppender createAppender(@PluginAttribute("name") String name,
                                                 @PluginElement("Filter") Filter filter,
                                                 @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                 @PluginAttribute("publishClass") String publishClass) {
        if (name == null) {
            LOGGER.error("No name provided for WhiteLog4jAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new WhiteLog4jAppender(name,filter,layout,publishClass);

    }
}
