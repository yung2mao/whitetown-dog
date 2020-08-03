package cn.whitetown.monitor.sys.runner;

import java.util.concurrent.Callable;

/**
 * 执行数据采集的线程
 * @author taixian
 * @date 2020/08/02
 **/
public interface SysMonCollectCall<T> extends Callable<T> {
}
