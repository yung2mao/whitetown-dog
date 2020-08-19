package cn.whitetown.logserver.manager.define;

import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.esconfig.manager.EsDocManager;
import cn.whitetown.esconfig.manager.EsIndicesManager;
import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logbase.config.LogUtil;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.modo.OpBaseLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 接口操作基础日志处理
 * @author taixian
 * @date 2020/08/13
 **/
public class OpBaseAnalyzer extends DefaultLogAnalyzer {

    private Log logger = LogFactory.getLog(OpBaseAnalyzer.class);

    private Logger localLogger = LogConstants.LOCAL_LOG_LOGGER;

    private BlockingQueue<OpBaseLog> logQueue = new ArrayBlockingQueue<>(LogConstants.LOG_CACHE_MAX_LEN);

    @Autowired
    private EsDocManager docManager;

    @Autowired
    private EsIndicesManager indicesManager;

    @Autowired
    private SnowIDCreateUtil idCreateUtil;

    @Override
    public void analyzer(WhLog whLog) {
        OpBaseLog opBaseLog = new OpBaseLog();
        opBaseLog.setLogLevel(LogUtil.num2Level(whLog.getLogLevel()).toString());
        String logData = whLog.getLogData();
        if(logData == null) {
            return;
        }
        try {
            String[] paramArr = logData.split("\t");
            String opLog = paramArr[paramArr.length - 1];
            this.detailAnalyzer(opBaseLog, opLog);
            long waitingTime = 5;
            boolean isOffer = logQueue.offer(opBaseLog, waitingTime, TimeUnit.SECONDS);
            if(!isOffer) {
                this.printOldLog();
                logQueue.offer(opBaseLog);
            }
            this.save();
        }catch (Exception e) {
            super.errorHandle(whLog, e);
        }
    }

    @Override
    public void save() {
        if(logQueue.size() < LogConstants.BATCH_SAVE_LEN) {
            return;
        }
        List<OpBaseLog> logs = new ArrayList<>();
        List<Map.Entry<String,OpBaseLog>> sources = new LinkedList<>();
        int count = logQueue.drainTo(logs);
        try {
            boolean exists = indicesManager.entityIndexExists(logs.get(0));
            //create index if not exists
            if (!exists) {
                indicesManager.createIndex(logs.get(0));
            }
            if (!exists) {
                return;
            }
            logs.forEach(log -> sources.add(new AbstractMap.SimpleEntry<>(idCreateUtil.getSnowId() + "", log)));
            docManager.addBatch(sources, null);
            logger.info("save operation base log, the count is " + count);
        }catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void destroy() {
        logQueue.forEach(log->localLogger.warn(log));
    }

    /**
     * 日志处理
     * @param opBaseLog
     * @param opLog
     */
    private void detailAnalyzer(OpBaseLog opBaseLog, String opLog) {
        String[] paramArr = opLog.split("\\|");
        int paramLen = 8;
        if(paramArr.length != paramLen) {
            return;
        }
        //logId | userId | requestTime |  uri | clientIp | browser | status | responseTime
        opBaseLog.setId(Long.parseLong(paramArr[0]));
        opBaseLog.setUserId(paramArr[1]);
        long requestTime = Long.parseLong(paramArr[2]);
        opBaseLog.setRequestTime(WhiteFormatUtil.millisToDate(requestTime));
        String uri = paramArr[3];
        String uriPrefix = uri.substring(0,uri.indexOf("/",1));
        opBaseLog.setUri(uri);
        opBaseLog.setUriPrefix(uriPrefix);
        opBaseLog.setClientIp(paramArr[4]);
        opBaseLog.setBrowser(paramArr[5]);
        opBaseLog.setResStatus(paramArr[6]);
        long responseTime = Long.parseLong(paramArr[7]);
        opBaseLog.setResTime(responseTime - requestTime);
    }

    /**
     * 打印旧日志信息
     */
    private void printOldLog() {
        int getLen = 50;
        List<OpBaseLog> logs = new ArrayList<>();
        logQueue.drainTo(logs,getLen);
        logs.forEach(log->localLogger.warn(log));
        logger.info("日志队列阻塞,已转储本地文件");
    }
}
