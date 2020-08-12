package cn.whitetown.logserver.manager;

import cn.whitetown.logbase.pipe.modo.WhLog;

/**
 * 日志服务端处理器
 * @author taixian
 * @date 2020/08/10
 **/
public interface WhLogAnalyzer {

    /**
     * 日志分析处理
     * @param whLog
     */
    void analyzer(WhLog whLog);

    /**
     * 日志存储
     */
    void save();
}