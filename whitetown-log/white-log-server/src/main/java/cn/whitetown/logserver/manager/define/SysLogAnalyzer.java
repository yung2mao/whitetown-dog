package cn.whitetown.logserver.manager.define;

import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import cn.whitetown.logserver.modo.SystemLog;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统运行日志处理
 * @author taixian
 * @date 2020/08/10
 **/
public class SysLogAnalyzer implements WhLogAnalyzer {

    @Autowired
    SnowIDCreateUtil idCreateUtil;

    @Override
    public void analyzer(WhLog whLog) {
        SystemLog systemLog = new SystemLog();
        systemLog.setId(idCreateUtil.getSnowId());
        systemLog.setLogName(whLog.getLogName());
        systemLog.setLogLevel(Level.toLevel(whLog.getLogLevel()).toString());
        systemLog.setPublishTime(whLog.getTimeStamp());
        String data = whLog.getLogData();
        try {
            systemLog = this.logDataAnalyzer(systemLog,data);
        }catch (Exception ignored) {
        }
        System.out.println(JSON.toJSONString(systemLog));
    }

    @Override
    public void save() {

    }

    private SystemLog logDataAnalyzer(SystemLog systemLog, String data) {
        if(data == null) {
            return systemLog;
        }
        //#时间 日志级别 线程名 启动到打印日志时间 类 方法 行数 : 日志内容
        String[] dataArr = data.split("\t");
        systemLog.setLogTime(WhiteFormatUtil.text2Date(dataArr[0]));
        systemLog.setLogThread(dataArr[2]);
        systemLog.setLogTimeToStart(Long.parseLong(dataArr[3]));
        systemLog.setLogClass(dataArr[4]);
        systemLog.setLogMethod(dataArr[5]);
        systemLog.setLogLine(dataArr[6]);
        systemLog.setMessage(dataArr[dataArr.length-1]);
        return systemLog;
    }
}
