package cn.whitetown.dogbase.wache.buffer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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

    protected BlockingQueue<BufferElement<E>> currentEleQueue;
    protected BlockingQueue<BufferElement<E>> allEleQueue;
    protected PoolEleFactory<E> eleFactory;

    protected AtomicInteger currentEleSize;
    protected AtomicInteger allEleSize;

    public AbstractBufferPool(int minIdle, int maxActive, long keepActive, TimeUnit timeUnit) {
        this.minIdle = minIdle;
        this.maxActive = maxActive;
        this.keepActive = keepActive;
        this.timeUnit = timeUnit;
        currentEleQueue = new ArrayBlockingQueue<>(maxActive << 1);
        allEleQueue = new ArrayBlockingQueue<>(maxActive << 1);
        currentEleSize = new AtomicInteger();
        allEleSize = new AtomicInteger();
    }

    public AbstractBufferPool() {
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
        try {
            currentEleQueue.clear();
            allEleQueue.clear();
        }catch (Exception e){
        }finally {
            currentEleQueue = null;
            allEleQueue = null;
        }

        currentEleSize = null;
        allEleSize = null;
    }
}
