package cn.whitetown.logserver.listener;

import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.listen.wiml.BaseWhListener;
import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import cn.whitetown.logserver.manager.LogAnalyzerMap;
import cn.whitetown.logserver.manager.define.DefaultLogAnalyzer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务端日志监听
 * @author taixian
 * @date 2020/08/09
 **/
public class DefaultLogListener extends BaseWhListener<WhLog> {

    private WhPipeline<WhLog> whPipeline;

    private LogAnalyzerMap logAnalyzerMap;

    /**
     * 解析处理器是否正常
     */
    private boolean analyzerIsOk = true;
    /**
     * 失败恢复时间
     */
    private long restoreTime = 60000;
    /**
     * 重试次数
     */
    private final int retryTimes = 5;
    AtomicInteger reTimes = new AtomicInteger(retryTimes);
    /**
     * 前次失败时间
     */
    long lastFailTime = 0;

    public DefaultLogListener(WhPipeline<WhLog> whPipeline, LogAnalyzerMap logAnalyzerMap) {
        this.whPipeline = whPipeline;
        this.logAnalyzerMap = logAnalyzerMap;
    }

    @Override
    public void listener(ListenerManager listenerManager, WhPipeline<WhLog> whPipeline) {
        this.listener();
    }

    @Override
    public void listener() {
        WhLog whLog = whPipeline.getAndRemove();
        while (whLog != null) {
            String logName = whLog.getLogName();
            this.handOut(logName,whLog);
            whLog = whPipeline.getAndRemove();
        }
    }

    @Override
    public void destroy(ListenerManager listenerManager) {
        logAnalyzerMap.getAll().forEach(WhLogAnalyzer::destroy);
        super.destroy(listenerManager);
    }

    /**
     * 日志信息分发处理
     * @param logName
     * @param whLog
     */
    private void handOut(String logName,WhLog whLog) {
        try {
            if(!checkAnalyzer()) {
                System.out.println(whLog);
                return;
            }
            WhLogAnalyzer logAnalyzer = logAnalyzerMap.getAnalyzer(logName);
            if(logAnalyzer == null) {
                logAnalyzer = new DefaultLogAnalyzer();
            }
            logAnalyzer.analyzer(whLog);
        }catch (Exception e) {
            System.err.println(e.getMessage());
            if(reTimes.decrementAndGet() < 1) {
                analyzerIsOk = false;
                lastFailTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * 校验当前日志解析器状态
     * @return
     */
    private boolean checkAnalyzer() {
        if(analyzerIsOk) {
            return true;
        }
        long nowTime = System.currentTimeMillis();
        if(nowTime - lastFailTime < restoreTime) {
            return false;
        }
        synchronized (this) {
            restoreTime += 1000;
            if(nowTime - lastFailTime > restoreTime) {
                reTimes.set(retryTimes);
                lastFailTime = nowTime;
                return true;
            }
            return false;
        }
    }
}
