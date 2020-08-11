package cn.whitetown.dogbase.wache.buffer;

import cn.whitetown.dogbase.wache.BufferElement;
import cn.whitetown.dogbase.wache.BufferPool;
import cn.whitetown.dogbase.wache.PoolEleFactory;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author taixian
 * @date 2020/08/11
 **/
public abstract class AbstractBufferPool<E> implements BufferPool<E> {

    protected int minIdle;
    protected int maxActive;
    protected long keepActive;
    protected TimeUnit timeUnit;

    protected Queue<BufferElement<E>> currentEleQueue;
    protected Queue<BufferElement<E>> allEleQueue;
    protected PoolEleFactory<E> eleFactory;

    protected AtomicInteger currentEleSize;
    protected AtomicInteger allEleSize;

    AbstractBufferPool(int minIdle, int maxActive, long keepActive, TimeUnit timeUnit) {
        this();
        this.minIdle = minIdle;
        this.maxActive = maxActive;
        this.keepActive = keepActive;
        this.timeUnit = timeUnit;
    }

    AbstractBufferPool() {
        currentEleQueue = new ArrayDeque<>(maxActive);
        allEleQueue = new ArrayDeque<>(maxActive);
        currentEleSize = new AtomicInteger();
        allEleSize = new AtomicInteger();
    }

    @Override
    public void initPoolEleFactory(PoolEleFactory<E> ePoolEleFactory) {
        if(ePoolEleFactory == null) {
            throw new NullPointerException("poolEleFactory is not init");
        }
        this.eleFactory = ePoolEleFactory;
        this.init();
    }

    @Override
    public void init() {
        for (int i = 0; i < minIdle; i++) {
            BufferElement<E> bufferElement = eleFactory.createPoolElement();
            currentEleQueue.add(bufferElement);
            allEleQueue.add(bufferElement);
        }
        currentEleSize.set(currentEleQueue.size());
        allEleSize.set(allEleQueue.size());
    }

    @Override
    public void destroy() {
        currentEleQueue.clear();
        allEleQueue.clear();
    }
}
