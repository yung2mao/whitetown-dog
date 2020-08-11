package cn.whitetown.dogbase.wache;

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
     * 获取过期时间
     * @return
     */
    long getExpiredTime();

    /**
     * 元素关闭
     */
    void close();

}
