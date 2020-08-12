package cn.whitetown.dogbase.wache.buffer.wiml;

import cn.whitetown.dogbase.wache.buffer.BufferPool;
import cn.whitetown.dogbase.wache.buffer.PoolEleFactory;
import cn.whitetown.dogbase.wache.buffer.WhPoolConfig;

import java.util.concurrent.TimeUnit;

/**
 * 缓冲池信息统一构建
 * @author taixian
 * @date 2020/08/11
 **/
public class BufferPoolFactory {

    public static final BufferPoolFactory BU_POOL_FACTORY;

    static {
        BU_POOL_FACTORY = new BufferPoolFactory();
    }

    public <E> WhPoolConfig<E> createConfig(){
        return new WhPoolConfig<>();
    }

    public <E> WhPoolConfig<E> createConfig(int minIdle, int maxActive, long keepActive, TimeUnit timeUnit){
        return new WhPoolConfig<>(minIdle,maxActive,keepActive,timeUnit);
    }

    public <E> BufferPool<E> buildPool(BufferPool<E> bufferPool, PoolEleFactory<E> factory) {
        bufferPool.initPoolEleFactory(factory);
        return bufferPool;
    }
}
