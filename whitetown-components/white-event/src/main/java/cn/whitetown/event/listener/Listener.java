package cn.whitetown.event.listener;

import cn.whitetown.event.modo.WhiteEvent;

/**
 * @Author: taixian
 * @Date: created in 2020/11/08
 */
public interface Listener {

    <T> WhiteEvent<T> listener();

    void confirm();
}
