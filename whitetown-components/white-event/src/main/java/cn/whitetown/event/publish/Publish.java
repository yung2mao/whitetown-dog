package cn.whitetown.event.publish;

import cn.whitetown.event.modo.WhiteEvent;

/**
 * @Author: taixian
 * @Date: created in 2020/11/08
 */
public interface Publish {

    /**
     * 事件发布
     * @param event 事件
     * @param <T> data泛型
     * @return 发送成功与否
     */
    <T> boolean publish(WhiteEvent<T> event);
}
