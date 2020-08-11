package cn.whitetown.dogbase.wache.wmil;

import cn.whitetown.dogbase.wache.BufferElement;
import cn.whitetown.dogbase.wache.PoolEleFactory;
import cn.whitetown.dogbase.wache.BufferPool;
import cn.whitetown.logbase.config.LogConstants;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;

/**
 * 缓冲池元素工厂
 * @author taixian
 * @date 2020/08/11
 **/
public class WhPoolElleFactory<E> implements PoolEleFactory<E> {

    private static Logger logger = LogConstants.SYS_LOGGER;

    private Class<? extends BufferElement> claz;
    private BufferPool<E> bufferPool;
    private E baseElement;

    private WhPoolElleFactory(BufferElement<E> bufferElement, BufferPool<E> bufferPool, E baseElement) {
        this.claz = bufferElement.getClass();
        this.bufferPool = bufferPool;
        this.baseElement = baseElement;
    }

    @SuppressWarnings("unchecked")
    public static <E> WhPoolElleFactory<E> createPoolEleFactory(BufferElement<E> bufferElement, BufferPool<E> bufferPool,Class<E> eleClass) {
        try {
            return new WhPoolElleFactory<>(bufferElement,bufferPool,eleClass.newInstance());
        }catch (Exception e) {
            logger.fatal(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static <E> WhPoolElleFactory<E> createPoolEleFactory(BufferElement<E> bufferElement, BufferPool<E> bufferPool, E baseElement) {
        if(bufferPool == null) {
            throw new NullPointerException("bufferPool is not init");
        }
        if(bufferElement == null) {
            bufferElement = new DefaultBufferElement<>();
        }
        return new WhPoolElleFactory<>(bufferElement,bufferPool,baseElement);
    }

    public static <E> WhPoolElleFactory<E> createPoolEleFactory(BufferPool<E> bufferPool, E baseElement) {
        BufferElement<E> bufferElement = new DefaultBufferElement<>();
        return createPoolEleFactory(bufferElement,bufferPool,baseElement);
    }

    @Override
    public BufferElement<E> createPoolElement() {
        if(claz == null) {
            return new DefaultBufferElement<>();
        }
        try {
            BufferElement<E> bufferElement = claz.newInstance();
            bufferElement.init(bufferPool);
            bufferElement.setE(createElement());
            return bufferElement;
        }catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public E createElement() {
        if(baseElement == null) {
            return null;
        }
        String text = JSON.toJSONString(baseElement);
        return  (E)JSON.parseObject(text, baseElement.getClass());
    }
}
