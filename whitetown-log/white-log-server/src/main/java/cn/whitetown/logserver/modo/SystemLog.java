package cn.whitetown.logserver.modo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author taixian
 * @date 2020/08/10
 **/
@Setter
@Getter
public class SystemLog {
    private Long id;
    private String logName;
    private String logLevel;
    private String logThread;
    /**
     * 记录时间 - 距离jvm启动的时间
     */
    private String logTimeToStart;
    /**
     * 记录时间 - 当前时间
     */
    private String logTime;
    private String logClass;
    private String logMethod;
    private String logLine;
    private String message;
    /**
     * 发送时间
     */
    private String publishTime;
}
