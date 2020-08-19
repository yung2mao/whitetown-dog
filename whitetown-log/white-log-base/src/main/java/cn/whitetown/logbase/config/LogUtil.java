package cn.whitetown.logbase.config;

import org.apache.logging.log4j.Level;

/**
 * @author taixian
 * @date 2020/08/19
 **/
public class LogUtil {
    public static Level num2Level(int levelInt) {
        if(levelInt == Level.TRACE.intLevel()) {
            return Level.TRACE;
        }
        if(levelInt == Level.ALL.intLevel()) {
            return Level.ALL;
        }
        if(levelInt == Level.DEBUG.intLevel()) {
            return Level.DEBUG;
        }
        if(levelInt == Level.INFO.intLevel()) {
            return Level.INFO;
        }
        if(levelInt == Level.WARN.intLevel()) {
            return Level.WARN;
        }
        if(levelInt == Level.ERROR.intLevel()) {
            return Level.ERROR;
        }
        if(levelInt == Level.FATAL.intLevel()) {
            return Level.FATAL;
        }
        if(levelInt == Level.OFF.intLevel()) {
            return Level.OFF;
        }
        return Level.forName("WHITE",levelInt);
    }
}
