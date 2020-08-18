package cn.whitetown.logserver.manager.define;

import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.esconfig.manager.EsDocManager;
import cn.whitetown.esconfig.manager.EsIndicesManager;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.modo.OpDetailLog;
import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/**
 * 接口操作细节日志处理
 * @author taixian
 * @date 2020/08/13
 **/
public class OpDetailAnalyzer extends DefaultLogAnalyzer {

    private Log logger = LogFactory.getLog(OpDetailAnalyzer.class);

    @Autowired
    private EsDocManager docManager;

    @Autowired
    private EsIndicesManager indicesManager;

    @Autowired
    private SnowIDCreateUtil idCreateUtil;

    @Override
    public void analyzer(WhLog whLog) {
        OpDetailLog opDetailLog = new OpDetailLog();
        opDetailLog.setLogLevel(Level.toLevel(whLog.getLogLevel()).toString());
        try {
            String[] logArr = whLog.getLogData().split("\t");
            opDetailLog = this.detailAnalyzer(opDetailLog, logArr[logArr.length - 1]);
            if (opDetailLog.getStatus() == null) {
                opDetailLog.setStatus(whLog.getLogLevel() <= Level.WARN_INT ? 200 : 500);
            }
            this.save(opDetailLog);
        } catch (Exception e) {
            logger.error(e.getMessage());
            super.errorHandle(whLog);
        }
    }

    private void save(OpDetailLog detailLog) throws IOException {
        boolean exists = indicesManager.entityIndexExists(detailLog);
        if(!exists) {
            indicesManager.createIndex(detailLog);
        }
        docManager.addDoc2DefaultIndex(idCreateUtil.getSnowId() +"",detailLog,null);
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