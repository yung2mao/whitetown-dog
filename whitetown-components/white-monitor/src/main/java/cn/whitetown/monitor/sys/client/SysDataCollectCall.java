package cn.whitetown.monitor.sys.client;

import java.util.concurrent.Callable;

/**
 * 执行数据采集的线程
 * @author taixian
 * @date 2020/08/02
 **/
public interface SysDataCollectCall<T> extends Callable<T> {
}
