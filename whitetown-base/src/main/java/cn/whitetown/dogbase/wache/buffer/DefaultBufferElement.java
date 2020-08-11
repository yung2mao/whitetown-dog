package cn.whitetown.dogbase.wache.buffer;

import cn.whitetown.dogbase.wache.BufferElement;
import cn.whitetown.dogbase.wache.BufferPool;

/**
 * @author taixian
 * @date 2020/08/11
 **/
public class DefaultBufferElement<E> implements BufferElement<E> {

    private BufferPool<E> pool;

    private E e;

    private long expiredTime;


    @Override
    public void init(BufferPool<E> bufferPool) {
        this.pool = bufferPool;
    }

    @Override
    public E getElement() {
        return this.e;
    }

    @Override
    public void setE(E e) {
        this.e = this.e == null ? e : this.e;
    }

    public void setExpireTime(long expiredTime) {
        long expire = System.currentTimeMillis() + expiredTime;
        this.expiredTime = expire < 0 ? Long.MAX_VALUE : expire;
    }

    @Override
    public long getExpiredTime() {
        return this.expiredTime;
    }

    @Override
    public void close() {
        pool.returnElement(this);
    }

}
