package cn.whitetown.logserver.manager;

import cn.whitetown.logserver.manager.WhLogAnalyzer;

import java.util.Map;

/**
 * 存储日志分析处理器的容器
 * @author taixian
 * @date 2020/08/13
 **/
public class LogAnalyzerMap {
    private Map<String, WhLogAnalyzer> logAnalyzerMap;

    public LogAnalyzerMap(Map<String,WhLogAnalyzer> logAnalyzerMap) {
        this.logAnalyzerMap = logAnalyzerMap;
    }

    public WhLogAnalyzer addAnalyzer(String analyzerName, WhLogAnalyzer whLogAnalyzer) {
        return logAnalyzerMap.put(analyzerName,whLogAnalyzer);
    }

    public WhLogAnalyzer getAnalyzer(String analyzerName) {
        return logAnalyzerMap.get(analyzerName);
    }
}
