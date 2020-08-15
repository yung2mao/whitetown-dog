package cn.whitetown.logserver.config;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import cn.whitetown.logserver.manager.define.*;
import cn.whitetown.logserver.manager.LogAnalyzerMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

/**
 * 日志分析处理器初始化
 * @author taixian
 * @date 2020/08/13
 **/
@Configuration
public class LogAnalyzerConfig {

    @Autowired
    private LogAnalyzerMap logAnalyzerMap;

    @Bean
    public LogAnalyzerMap logAnalyzerMap() {
        return new LogAnalyzerMap(new HashMap<>(4));
    }

    @Bean(LogConstants.SYS_LOG)
    public WhLogAnalyzer sysLogAnalyzer() {
        SysLogAnalyzer sysLogAnalyzer = new SysLogAnalyzer();
        logAnalyzerMap.addAnalyzer(LogConstants.SYS_LOG,sysLogAnalyzer);
        return sysLogAnalyzer;
    }

    @Bean(LogConstants.OP_BASE_LOG)
    public WhLogAnalyzer opBaseAnalyzer() {
        OpBaseAnalyzer opBaseAnalyzer = new OpBaseAnalyzer();
        logAnalyzerMap.addAnalyzer(LogConstants.OP_BASE_LOG,opBaseAnalyzer);
        return opBaseAnalyzer;
    }

    @Bean(LogConstants.OP_DETAIL_LOG)
    public WhLogAnalyzer opDetailAnalyzer() {
        OpDetailAnalyzer detailAnalyzer = new OpDetailAnalyzer();
        logAnalyzerMap.addAnalyzer(LogConstants.OP_DETAIL_LOG,detailAnalyzer);
        return detailAnalyzer;
    }

    @Bean(LogConstants.ACC_LOG)
    public WhLogAnalyzer accLogAnalyzer() {
        AccLogAnalyzer accLogAnalyzer = new AccLogAnalyzer();
        logAnalyzerMap.addAnalyzer(LogConstants.ACC_LOG,accLogAnalyzer);
        return accLogAnalyzer;
    }

    @Bean(LogConstants.DEFAULT_LOG)
    public WhLogAnalyzer defaultAnalyzer() {
        DefaultLogAnalyzer defaultLogAnalyzer = new DefaultLogAnalyzer();
        logAnalyzerMap.addAnalyzer(LogConstants.DEFAULT_LOG,defaultLogAnalyzer);
        return defaultLogAnalyzer;
    }
}
