package cn.whitetown.logbase.pipe.modo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 日志传输信息
 * @author taixian
 * @date 2020/08/09
 **/
public class WhLog extends WhClone {
    private String logName;
    private int logLevel;
    private String logData;
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date timeStamp;

    public WhLog() {
    }

    public WhLog(String logName, int logLevel, String logData, Date timeStamp) {
        this.logName = logName;
        this.logLevel = logLevel;
        this.logData = logData;
        this.timeStamp = timeStamp;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogData() {
        return logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
