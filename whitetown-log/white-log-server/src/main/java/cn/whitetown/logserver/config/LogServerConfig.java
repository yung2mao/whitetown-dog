package cn.whitetown.logserver.config;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logbase.listen.WhListener;
import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.listener.DefaultLogListener;
import cn.whitetown.logserver.manager.LogAnalyzerMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PreDestroy;

/**
 * 服务端配置类
 * @author taixian
 * @date 2020/08/10
 **/
@Configuration
public class LogServerConfig {

    @Autowired
    private LogAnalyzerMap logAnalyzerMap;

    @Autowired
    @Lazy
    private WhListener<WhLog> whLogWhListener;

    /**
     * 初始化监听器
     * @return
     */
    @Bean
    public WhListener<WhLog> whLogWhListener() {
        DefaultLogListener logListener = new DefaultLogListener(LogConstants.LOG_PIPELINE,logAnalyzerMap);
        logListener.registry(LogConstants.LISTENER_MANAGER);
        logListener.listener();
        return logListener;
    }

    @PreDestroy
    public void destroy() {
        whLogWhListener.destroy(LogConstants.LISTENER_MANAGER);
    }

}
