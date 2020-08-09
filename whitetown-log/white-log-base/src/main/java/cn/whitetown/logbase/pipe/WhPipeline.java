package cn.whitetown.logbase.pipe;

import java.util.Collection;

/**
 * 数据管道
 * @author taixian
 * @date 2020/08/09
 **/
public interface WhPipeline<E> {

    /**
     * 从管道获取并移除元素
     * @return
     */
    E takeRecElement();

    /**
     * 从管道获取数据-不移除
     * @return
     */
    E peekElement();

    /**
     * 获取所有元素并放入指定集合
     * @param c
     * @return
     */
    int drainTo(Collection<? super E> c);

    /**
     * 向管道添加元素
     * @param e
     * @return
     */
    boolean addElement(E e);

    /**
     * 管道当前容量
     * @return
     */
    int size();

    /**
     * 管道最大容量
     * @return
     */
    int maxSize();

    /**
     * 数据清理
     */
    void clear();

    /**
     * 销毁
     */
    void destroy();
}
