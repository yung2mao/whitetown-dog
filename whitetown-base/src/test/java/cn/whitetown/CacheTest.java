package cn.whitetown;

import cn.whitetown.dogbase.wache.BufferPool;
import cn.whitetown.dogbase.wache.conf.WhPoolConfig;
import cn.whitetown.dogbase.wache.wmil.BaseBufferPool;
import cn.whitetown.dogbase.wache.wmil.BufferPoolFactory;
import cn.whitetown.dogbase.wache.wmil.DefaultBufferElement;
import cn.whitetown.dogbase.wache.wmil.WhPoolElleFactory;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author taixian
 * @date 2020/08/11
 **/
public class CacheTest {

    @Test
    public void cacheTest01() {
        WhPoolConfig<byte[]> poolConfig = new WhPoolConfig<>(5,10,60,
                TimeUnit.SECONDS);
        BufferPool<byte[]> bufferPool = BaseBufferPool.createBasePool(poolConfig);
        WhPoolElleFactory<byte[]> factory = WhPoolElleFactory.createPoolEleFactory(bufferPool, new byte[1024]);
        BufferPoolFactory.BU_POOL_FACTORY.buildPool(bufferPool,factory);

        byte[] element = bufferPool.getElement().getElement();
        byte[] element1 = bufferPool.getElement().getElement();
        byte[] element2 = bufferPool.getElement().getElement();
        byte[] element3 = bufferPool.getElement().getElement();
        System.out.println(element + "," + element.length);
        System.out.println(element1 + "," + element.length);
        System.out.println(element2 + "," + element.length);
        System.out.println(element3 + "," + element.length);
    }
}
