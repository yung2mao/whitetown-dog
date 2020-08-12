package cn.whitetown;

import cn.whitetown.dogbase.wache.buffer.*;
import cn.whitetown.dogbase.wache.buffer.wiml.BufferPoolFactory;
import cn.whitetown.dogbase.wache.buffer.wiml.ByteBufferElement;
import org.junit.Test;

/**
 * @author taixian
 * @date 2020/08/11
 **/
public class CacheTest {

    @Test
    public void cacheTest01() {
        BufferPoolFactory buPoolFactory = BufferPoolFactory.BU_POOL_FACTORY;
        WhPoolConfig<byte[]> config = buPoolFactory.createConfig();
        BufferPool<byte[]> basePool = BaseBufferPool.createBasePool(config);
        WhPoolEleFactory<byte[]> factory = WhPoolEleFactory.createPoolEleFactory(new ByteBufferElement(), basePool, new byte[1024]);
        BufferPool<byte[]> bufferPool = buPoolFactory.buildPool(basePool, factory);

        BufferElement<byte[]> element = bufferPool.getElement();
        System.out.println(element.getElement().length);
    }
}
