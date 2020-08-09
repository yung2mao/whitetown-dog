package cn.whitetown.logbase.listen;

/**
 * @author taixian
 * @date 2020/08/09
 **/
public interface DataChange<T> {
    /**
     * 数据更新调用
     * @param t
     */
    void update(T t);
}
