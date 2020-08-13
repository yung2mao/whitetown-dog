package cn.whitetown.logserver.modo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private long logTimeToStart;
    /**
     * 记录时间 - 当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss.SSS")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss.SSS")
    private Date logTime;
    private String logClass;
    private String logMethod;
    private String logLine;
    private String message;
    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss.SSS")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss.SSS")
    private Date publishTime;
}
