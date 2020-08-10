package cn.whitetown.logserver.manager.define;

import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;

/**
 * @author taixian
 * @date 2020/08/10
 **/
public class DefaultLogAnalyzer implements WhLogAnalyzer {
    @Override
    public void analyzer(WhLog whLog) {
        System.out.println(whLog);
    }

    @Override
    public void save() {

    }
}
