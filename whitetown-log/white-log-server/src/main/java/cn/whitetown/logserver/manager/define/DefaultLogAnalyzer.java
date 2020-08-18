package cn.whitetown.logserver.manager.define;

import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 默认日志处理
 * @author taixian
 * @date 2020/08/10
 **/
public class DefaultLogAnalyzer implements WhLogAnalyzer {

    @Override
    public void analyzer(WhLog whLog) {
        if (whLog.getLogLevel() < Level.ERROR_INT) {
            System.out.println(whLog);
            return;
        }
        System.err.println(whLog);
    }

    @Override
    public void save() {
    }

    @Override
    public void destroy() {
    }
}
