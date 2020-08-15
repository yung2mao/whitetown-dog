package cn.whitetown.logserver.manager.define;

import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import cn.whitetown.logserver.modo.OpDetailLog;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * 接口操作细节日志处理
 * @author taixian
 * @date 2020/08/13
 **/
public class OpDetailAnalyzer implements WhLogAnalyzer {

    private Logger logger = LogConstants.SYS_LOGGER;

    @Override
    public void analyzer(WhLog whLog) {
        OpDetailLog opDetailLog = new OpDetailLog();
        opDetailLog.setLogLevel(Level.toLevel(whLog.getLogLevel()).toString());
        try {
            String[] logArr = whLog.getLogData().split("\t");
            opDetailLog = this.detailAnalyzer(opDetailLog, logArr[logArr.length - 1]);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
        if (opDetailLog.getStatus() == null) {
            opDetailLog.setStatus(whLog.getLogLevel() <= Level.WARN_INT ? 200 : 500);
        }

        System.out.println(opDetailLog);
    }

    @Override
    public void save() {
    }

    private OpDetailLog detailAnalyzer(OpDetailLog detailLog, String data) {
        String[] logArr = data.split("\\|");
        int detailLen = 3;
        if (logArr.length < detailLen) {
            detailLog.setMessage(data);
            return detailLog;
        }
        detailLog.setId(Long.parseLong(logArr[0]));
        detailLog.setLogTime(WhiteFormatUtil.millisToDate(Long.parseLong(logArr[1])));
        String detail = logArr[2];
        String resultStatus = "status";
        String statusMessage = "statusName";
        if (detail.contains(resultStatus) && detail.contains(statusMessage)) {
            try {
                Map messageMap = JSON.parseObject(detail, Map.class);
                Object status = messageMap.get("status");
                detailLog.setStatus(Integer.parseInt(String.valueOf(status)));
                detailLog.setMessage(String.valueOf(messageMap.get("statusName")));
                return detailLog;
            } catch (Exception ignored) {
            }
        }
        detailLog.setMessage(detail);
        return detailLog;
    }

}