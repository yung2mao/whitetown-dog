package cn.whitetown.logbase.pipe.wiml;

import cn.whitetown.logbase.pipe.WhPipeline;
import cn.whitetown.logbase.pipe.modo.WhClone;
import cn.whitetown.logbase.pipe.modo.WhLog;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 日志缓存队列
 * @author taixian
 * @date 2020/08/09
 **/
public class WhLogPipeline extends WhClone implements WhPipeline<WhLog> {

    private BlockingQueue<WhLog> logQueue;

    private WhLogPipeline(BlockingQueue<WhLog> logQueue) {
        this.logQueue = logQueue;
    }

    public static WhLogPipeline getInstance(BlockingQueue<WhLog> logQueue) {
        return new WhLogPipeline(logQueue);
    }

    public static WhLogPipeline getDefaultPipeline() {
        int capacity = 2048;
        return getInstance(new ArrayBlockingQueue<>(capacity));
    }

    @Override
    public WhLog takeRecElement() {
        return logQueue.poll();
    }

    @Override
    public WhLog peekElement() {
        return logQueue.peek();
    }

    @Override
    public int drainTo(Collection<? super WhLog> c) {
        return logQueue.drainTo(c);
    }

    @Override
    public boolean addElement(WhLog whLog) {
        long timeout = 5;
        try {
            logQueue.offer(whLog,timeout, TimeUnit.SECONDS);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public int size() {
        return logQueue.size();
    }

    @Override
    public int maxSize() {
        return logQueue.remainingCapacity();
    }

    @Override
    public void clear() {
        logQueue.clear();
    }

    @Override
    public void destroy() {
        logQueue.clear();
        logQueue = null;
    }

}
