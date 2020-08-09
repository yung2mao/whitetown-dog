package cn.whitetown.logbase.pipe.modo;

import com.alibaba.fastjson.JSON;

/**
 * 日志传输信息
 * @author taixian
 * @date 2020/08/09
 **/
public class WhLog {
    private String logName;
    private String logData;
    private String timeStamp;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogData() {
        return logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
