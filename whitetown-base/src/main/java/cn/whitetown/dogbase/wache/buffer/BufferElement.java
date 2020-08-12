package cn.whitetown.dogbase.wache.buffer;

/**
 * 缓存池基本元素
 * @author taixian
 * @date 2020/08/11
 **/
public interface BufferElement<E> {

    /**
     * 初始化方法
     * @param bufferPool
     */
    void init(BufferPool<E> bufferPool);

    /**
     * 获取元素
     * @return
     */
    E getElement();

    /**
     * 设置元素
     * @param e
     */
    void setE(E e);

    /**
     * 元素关闭
     */
    void close();

}
