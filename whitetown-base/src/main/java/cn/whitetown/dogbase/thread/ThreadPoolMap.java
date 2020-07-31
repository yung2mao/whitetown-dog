package cn.whitetown.dogbase.thread;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author taixian
 * @date 2020/07/31
 **/
public class ThreadPoolMap {
    private final int maxPoolSize;
    private int currentSize;
    private final Map<String, ExecutorService> threadPoolMap;

    public ThreadPoolMap(int maxPoolSize, Map<String, ExecutorService> threadPoolMap) {
        this.maxPoolSize = maxPoolSize;
        this.threadPoolMap = threadPoolMap;
    }

    public synchronized void putThreadPool(String threadName,ExecutorService executorService) {
        if(executorService == null || currentSize >= maxPoolSize) {
            return;
        }
        threadPoolMap.put(threadName,executorService);
        currentSize ++;
    }

    public ExecutorService getThreadPool(String name) {
        return threadPoolMap.get(name);
    }
}
