package cn.whitetown.logserver.manager.define;

import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import cn.whitetown.logserver.modo.OpBaseLog;
import org.apache.log4j.Level;

/**
 * 接口操作基础日志处理
 * @author taixian
 * @date 2020/08/13
 **/
public class OpBaseAnalyzer implements WhLogAnalyzer {
    @Override
    public void analyzer(WhLog whLog) {
        OpBaseLog opBaseLog = new OpBaseLog();
        opBaseLog.setLogLevel(Level.toLevel(whLog.getLogLevel()).toString());
        String logData = whLog.getLogData();
        if(logData == null) {
            return;
        }
        try {
            String[] paramArr = logData.split("\t");
            String opLog = paramArr[paramArr.length - 1];
            opBaseLog = this.opBaseLog(opBaseLog, opLog);
        }catch (Exception e) {
            e.printStackTrace();
            String detail = whLog.getLogData();
            detail = detail.replaceAll("\t"," ").replaceAll("\\|"," ");
            opBaseLog.setDetails(detail);
        }
        System.out.println(opBaseLog);
    }

    @Override
    public void save() {
    }

    private OpBaseLog opBaseLog(OpBaseLog opBaseLog,String opLog) {
        String[] paramArr = opLog.split("\\|");
        int paramLen = 8;
        if(paramArr.length != paramLen) {
            return opBaseLog;
        }
        //logId | userId | requestTime |  uri | clientIp | browser | status | responseTime
        opBaseLog.setId(Long.parseLong(paramArr[0]));
        opBaseLog.setUserId(paramArr[1]);
        long requestTime = Long.parseLong(paramArr[2]);
        opBaseLog.setRequestTime(WhiteFormatUtil.millisToDate(requestTime));
        String uri = paramArr[3];
        String uriPrefix = uri.substring(0,uri.indexOf("/"));
        opBaseLog.setUri(uri);
        opBaseLog.setUriPrefix(uriPrefix);
        opBaseLog.setClientIp(paramArr[4]);
        opBaseLog.setBrowser(paramArr[5]);
        opBaseLog.setResStatus(paramArr[6]);
        long responseTime = Long.parseLong(paramArr[7]);
        opBaseLog.setResTime(responseTime - requestTime);
        return opBaseLog;
    }
}
