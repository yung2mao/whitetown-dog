package cn.whitetown.logserver.listener;

import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.listen.wiml.BaseWhListener;
import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import cn.whitetown.logserver.manager.LogAnalyzerMap;
import cn.whitetown.logserver.manager.define.DefaultLogAnalyzer;

/**
 * 服务端日志监听
 * @author taixian
 * @date 2020/08/09
 **/
public class DefaultLogListener extends BaseWhListener<WhLog> {

    private WhPipeline<WhLog> whPipeline;

    private LogAnalyzerMap logAnalyzerMap;

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
        WhLogAnalyzer logAnalyzer = null;
        try {
            logAnalyzer = logAnalyzerMap.getAnalyzer(logName);
            if(logAnalyzer == null) {
                logAnalyzer = new DefaultLogAnalyzer();
            }
            if(logAnalyzer.status()) {
                logAnalyzer.analyzer(whLog);
            }else {
                logAnalyzer.errorHandle(whLog,null);
            }
        }catch (Exception e) {
            assert logAnalyzer != null;
            logAnalyzer.errorHandle(whLog,e);
        }
    }
}
