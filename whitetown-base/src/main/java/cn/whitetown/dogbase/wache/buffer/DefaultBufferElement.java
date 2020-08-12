package cn.whitetown.dogbase.wache.buffer;

/**
 * @author taixian
 * @date 2020/08/11
 **/
public class DefaultBufferElement<E> implements BufferElement<E> {

    protected BufferPool<E> pool;

    protected E e;


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

    @Override
    public void close() {
        pool.returnElement(this);
    }

}
