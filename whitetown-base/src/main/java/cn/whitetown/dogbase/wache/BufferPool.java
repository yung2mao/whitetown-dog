package cn.whitetown.dogbase.wache;

/**
 * @author taixian
 * @date 2020/08/11
 **/
public interface BufferPool<E> extends WhiteCacheBase{

    /**
     * 初始化pool元素创建工厂
     * @param ePoolEleFactory
     */
    void initPoolEleFactory(PoolEleFactory<E> ePoolEleFactory);

    /**
     * 获取元素
     * @return
     */
    BufferElement<E> getElement();

    /**
     * 归还元素到池中
     * @param poolEle
     * @return
     */
    boolean returnElement(BufferElement<E> poolEle);
}
