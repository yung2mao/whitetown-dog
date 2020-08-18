package cn.whitetown.logserver.manager.define;

import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.esconfig.manager.EsDocManager;
import cn.whitetown.esconfig.manager.EsIndicesManager;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.modo.SystemLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 系统运行日志处理
 * @author taixian
 * @date 2020/08/10
 **/
public class SysLogAnalyzer extends DefaultLogAnalyzer {

    @Autowired
    SnowIDCreateUtil idCreateUtil;

    @Autowired
    EsIndicesManager indicesManager;

    @Autowired
    EsDocManager docManager;

    private Log log = LogFactory.getLog(SysLogAnalyzer.class);

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
        this.save(systemLog);
    }

    private void save(SystemLog sysLog) {
        boolean exists = indicesManager.entityIndexExists(sysLog);
        if(!exists) {
            exists = indicesManager.createIndex(sysLog);
        }
        if(!exists) {
            log.info(sysLog);
            return;
        }
        String docId = sysLog.getId() + "";
        docManager.addDoc2DefaultIndex(docId,sysLog,null);
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
        systemLog.setLogLine(Integer.parseInt(dataArr[6]));
        systemLog.setMessage(dataArr[dataArr.length-1]);
        return systemLog;
    }
}
