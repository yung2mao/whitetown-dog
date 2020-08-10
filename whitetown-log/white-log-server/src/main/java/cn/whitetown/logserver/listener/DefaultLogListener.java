package cn.whitetown.logserver.listener;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logbase.listen.ListenerManager;
import cn.whitetown.logbase.listen.wiml.BaseWhListener;
import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.LogAnalyzerEnum;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import org.apache.log4j.Logger;

/**
 * 服务端日志监听
 * @author taixian
 * @date 2020/08/09
 **/
public class DefaultLogListener extends BaseWhListener<WhLog> {

    private Logger logger = LogConstants.SYS_LOGGER;

    private WhPipeline<WhLog> whPipeline;


    public DefaultLogListener(WhPipeline<WhLog> whPipeline) {
        this.whPipeline = whPipeline;
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
            WhLogAnalyzer logAnalyzer = LogAnalyzerEnum.getLogAnalyzer(logName);
            logAnalyzer.analyzer(whLog);
            whLog = whPipeline.getAndRemove();
        }
        logger.debug("the current log processing is complete");
    }
}
