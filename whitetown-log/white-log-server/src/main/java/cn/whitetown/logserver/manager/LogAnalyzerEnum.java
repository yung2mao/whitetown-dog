package cn.whitetown.logserver.manager;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.logserver.manager.define.DefaultLogAnalyzer;
import cn.whitetown.logserver.manager.define.SysLogAnalyzer;

/**
 * @author taixian
 * @date 2020/08/10
 **/
public enum LogAnalyzerEnum {
    /**
     * 系统日志处理器
     */
    SYS_LOG(LogConstants.SYS_LOG,new SysLogAnalyzer());

    private String name;

    private WhLogAnalyzer logAnalyzer;

    LogAnalyzerEnum(String name, WhLogAnalyzer logAnalyzer) {
        this.name = name;
        this.logAnalyzer = logAnalyzer;
    }

    public static WhLogAnalyzer getLogAnalyzer(String name) {
        LogAnalyzerEnum logAnalyzerEnum = valueOf(name);
        return logAnalyzerEnum == null ? new DefaultLogAnalyzer() : logAnalyzerEnum.logAnalyzer;
    }
}
