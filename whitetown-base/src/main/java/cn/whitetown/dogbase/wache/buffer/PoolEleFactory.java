package cn.whitetown.dogbase.wache.buffer;

/**
 * PoolElement创建工厂
 * @author taixian
 * @date 2020/08/11
 **/
public interface PoolEleFactory<E> {
    /**
     * 创建PoolElement
     * @return
     */
    BufferElement<E> createPoolElement();

    /**
     * 创建element元素
     * @return
     */
    E createElement();
}
