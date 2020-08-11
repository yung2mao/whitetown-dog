package cn.whitetown.dogbase.wache.buffer;

import cn.whitetown.dogbase.wache.BufferElement;
import cn.whitetown.dogbase.wache.BufferPool;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.log4j.Logger;
import java.util.concurrent.TimeUnit;

/**
 * 默认元素池实现
 * @author taixian
 * @date 2020/08/11
 **/
public class BaseBufferPool<E> extends AbstractBufferPool<E> {

    private static Logger logger = LogConstants.SYS_LOGGER;

    public static <E> BufferPool<E> createBasePool() {
        return createBasePool(null);
    }

    BaseBufferPool(int minIdle, int maxActive, long keepActive, TimeUnit timeUnit) {
        super(minIdle, maxActive, keepActive, timeUnit);
    }

    BaseBufferPool() {
        super();
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
        BaseBufferPool<E> baseBufferPool = new BaseBufferPool<>();
        baseBufferPool.minIdle = poolConfig.getMinIdle();
        baseBufferPool.maxActive = poolConfig.getMaxActive();
        baseBufferPool.keepActive = poolConfig.getKeepActive();
        baseBufferPool.timeUnit = poolConfig.getTimeUnit();
        return baseBufferPool;
    }

    @Override
    public BufferElement<E> getElement() {
        return currentEleQueue.poll();
    }

    @Override
    public boolean returnElement(BufferElement<E> poolEle) {
        return false;
    }
    public void removeElement() {
    }
}
