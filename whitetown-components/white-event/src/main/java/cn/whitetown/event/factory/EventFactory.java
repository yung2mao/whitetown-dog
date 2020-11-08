package cn.whitetown.event.factory;

/**
 *
 * @Author: taixian
 * @Date: created in 2020/10/26
 */
public interface EventFactory {

    /**
     * 创建一个event
     * @param body
     * @param <T>
     */
    <T> void createEvent(T body);

    /**
     * 创建一个event，带有指定的标记
     * @param markKey
     * @param body
     * @param <T>
     */
    <T> void  createEvent(String markKey, T body);


}
