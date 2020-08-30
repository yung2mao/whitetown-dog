package cn.whitetown.monitor.util;

/**
 * 注册销毁时执行的线程
 * @author taixian
 * @date 2020/08/30
 **/
public class DestroyHook {

    public static void destroy(Runnable runnable) {
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(runnable));
    }
}
