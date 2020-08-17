package cn.whitetown.logserver.manager.define;

import cn.whitetown.logbase.pipe.modo.WhLog;
import cn.whitetown.logserver.manager.WhLogAnalyzer;

/**
 * 访问日志处理器
 * @author taixian
 * @date 2020/08/13
 **/
public class AccLogAnalyzer implements WhLogAnalyzer {
    @Override
    public void analyzer(WhLog whLog) {
        System.out.println(whLog);
    }

    @Override
    public void save() {
    }
}
