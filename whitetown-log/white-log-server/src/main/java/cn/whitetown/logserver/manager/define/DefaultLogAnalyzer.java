package cn.whitetown.logserver.manager.define;

import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author taixian
 * @date 2020/08/10
 **/
public class DefaultLogAnalyzer implements WhLogAnalyzer {

    @Autowired
    private SysLogAnalyzer sysLogAnalyzer;

    @Override
    public void analyzer(WhLog whLog) {
        sysLogAnalyzer.analyzer(whLog);
    }

    @Override
    public void save() {
    }
}
