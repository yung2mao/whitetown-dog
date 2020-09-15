package cn.whitetown.mshow.modo.log;

import cn.whitetown.esconfig.annotation.EsFieldConfig;
import cn.whitetown.esconfig.config.EsConfigEnum;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 系统日志
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date logTime;

    @EsFieldConfig(name = "message",config = {EsConfigEnum.TEXT,EsConfigEnum.ES_STANDARD})
    private String logClass;

    private String logMethod;

    private int logLine;

    @EsFieldConfig(name = "message",config = {EsConfigEnum.TEXT,EsConfigEnum.ES_IK})
    private String message;
    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date publishTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
