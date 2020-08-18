package cn.whitetown.dogbase.wache.buffer;

import cn.hutool.core.thread.ThreadUtil;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 默认元素池实现
 * @author taixian
 * @date 2020/08/11
 **/
public class BaseBufferPool<E> extends AbstractBufferPool<E> {

    private long beforeTime = System.currentTimeMillis();


    private static Logger logger = LogConstants.SYS_LOGGER;

    public static <E> BufferPool<E> createBasePool() {
        return createBasePool(null);
    }

    BaseBufferPool(int minIdle, int maxActive, long keepActive, TimeUnit timeUnit) {
        super(minIdle, maxActive, keepActive, timeUnit);
    }

    /**
     * 创建缓冲池 - 未初始化元素工厂
     * @param poolConfig
     * @param <E>
     * @return
     */
    public static <E> BufferPool<E> createBasePool(WhPoolConfig<E> poolConfig) {
        if(poolConfig == null) {
            BaseBufferPool<E> baseBufferPool = new BaseBufferPool<>(2, 10, 60, TimeUnit.SECONDS);
            baseBufferPool.initPoolEleFactory(null);
            baseBufferPool.init();
            return baseBufferPool;
        }
        return new BaseBufferPool<>(poolConfig.getMinIdle(),
                poolConfig.getMaxActive(),
                poolConfig.getKeepActive(),
                poolConfig.getTimeUnit());
    }

    @Override
    public BufferElement<E> getElement() {
        BufferElement<E> element = currentEleQueue.poll();
        if(element != null) {
            currentEleSize.decrementAndGet();
            return element;
        }
        this.expand();
        try {
            long waiting = 30;
            element = currentEleQueue.poll(waiting, TimeUnit.SECONDS);
            currentEleSize.decrementAndGet();
            return element;
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public boolean returnElement(BufferElement<E> poolEle) {
        boolean isContains = allEleQueue.contains(poolEle);
        if(isContains) {
            currentEleQueue.add(poolEle);
            currentEleSize.incrementAndGet();
            if(isReduce()) {
                ThreadUtil.execAsync(this::reduce);
            }
            return true;
        }
        return false;
    }

    /**
     * 判断是否需要缩容
     * @return
     */
    private boolean isReduce() {
        long nowTime = System.currentTimeMillis();
        long intervalTime = 60000;
        if((nowTime - beforeTime) > intervalTime ) {
            beforeTime = nowTime;
            return true;
        }
        return false;
    }

    /**
     * 缩容
     */
    private void reduce() {
        int currentSize = currentEleSize.get();
        if(currentSize < (minIdle << 1)) {
            return;
        }
        for (int i = 0; i < minIdle; i++) {
            BufferElement<E> removeEle = currentEleQueue.poll();
            allEleQueue.remove(removeEle);
            currentEleSize.decrementAndGet();
            allEleSize.decrementAndGet();
        }
    }

    /**
     * 扩容
     */
    private void expand() {
        if(allEleSize.get() >= maxActive) {
            return;
        }
        int expandSize = (maxActive - allEleSize.get()) >> 1;
        expandSize = expandSize > 0 ? expandSize : 1;
        for (int i = 0; i < expandSize; i++) {
            BufferElement<E> element = eleFactory.createPoolElement();
            currentEleQueue.add(element);
            allEleQueue.add(element);
            currentEleSize.incrementAndGet();
            allEleSize.incrementAndGet();
        }
    }
}
