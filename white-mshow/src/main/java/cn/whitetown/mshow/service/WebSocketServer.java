package cn.whitetown.mshow.service;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;

/**
 * websocket服务
 * @author taixian
 * @date 2020/08/25
 **/
public interface WebSocketServer {

    /**
     * 建立连接
     * @param session session中传递随机码,基于随机码快速查找实际访问的user
     */
    void onOpen(Session session);

    /**
     * 关闭连接调用
     * @param session 建立连接的session
     */
    void onClose(Session session);

    /**
     * 客户端发送消息
     * @param session
     * @param message
     */
    void onMessage(Session session, String message);

    /**
     * 发生错误
     * @param session
     * @param cause
     */
    void onError(Session session, Throwable cause);

    /**
     * 主动调用关闭连接
     * @param session
     */
    void disconnect(Session session);

}
