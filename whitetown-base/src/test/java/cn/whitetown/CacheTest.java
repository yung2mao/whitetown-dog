package cn.whitetown;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.wache.buffer.*;
import cn.whitetown.dogbase.wache.buffer.wiml.BufferPoolFactory;
import cn.whitetown.dogbase.wache.buffer.wiml.ByteBufferElement;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;

/**
 * @author taixian
 * @date 2020/08/11
 **/
public class CacheTest {

    @Test
    public void bufferEleTest() {
        BufferPoolFactory buPoolFactory = BufferPoolFactory.BU_POOL_FACTORY;
        WhPoolConfig<byte[]> config = buPoolFactory.createConfig();
        BufferPool<byte[]> basePool = BaseBufferPool.createBasePool(config);
        WhPoolEleFactory<byte[]> factory = WhPoolEleFactory.createPoolEleFactory(new ByteBufferElement(), basePool, new byte[1024]);
        BufferPool<byte[]> bufferPool = buPoolFactory.buildPool(basePool, factory);

        while (true) {
            BufferElement<byte[]> element = bufferPool.getElement();
            System.out.println(element +","+ element.getElement() +","+ element.getElement().length);
            this.returnEle(element);
            break;
        }
    }

    public void returnEle(BufferElement bufferElement){
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(5000);
                bufferElement.close();
            }
        }).start();
    }

    @Test
    public void test02(){
        Field[] declaredFields = PageQuery.class.getDeclaredFields();
        Arrays.stream(declaredFields).map(Field::getName).forEach(System.out::println);
    }
}
