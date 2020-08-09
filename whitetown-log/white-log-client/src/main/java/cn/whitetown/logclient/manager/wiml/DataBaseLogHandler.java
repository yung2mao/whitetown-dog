package cn.whitetown.logclient.manager.wiml;

import cn.whitetown.logclient.manager.WhiteLogHandler;
import org.apache.log4j.pattern.LogEvent;

/**
 * 日志数据库存储处理
 * @author taixian
 * @date 2020/08/06
 **/
public class DataBaseLogHandler implements WhiteLogHandler {
    @Override
    public void publish(String logName, String logData){
        System.out.println(logName + "," + logData);
    }

    @Override
    public void publish(LogEvent logEvent) {
    }
}
