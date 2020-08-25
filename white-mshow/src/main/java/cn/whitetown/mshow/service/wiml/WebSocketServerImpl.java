package cn.whitetown.mshow.service.wiml;

import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.mshow.manager.SocketCache;
import cn.whitetown.mshow.service.WebSocketServer;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * websocket服务默认实现
 * @author taixian
 * @date 2020/08/25
 **/
@Component
@ServerEndpoint("/ws/white")
public class WebSocketServerImpl implements WebSocketServer {

    private Logger logger = LogConstants.SYS_LOGGER;

    private static AtomicInteger connectCount = new AtomicInteger(0);

    private static Map<String,Session> userSession = new ConcurrentHashMap<>(2);

    /**
     * websocket是多例模型,不能直接注入
     */
    private SocketCache socketCache;

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public void initSocketCache() {
        socketCache = applicationContext.getBean(SocketCache.class);
    }

    @Override
    @OnOpen
    public void onOpen(Session session) {
        if(socketCache == null) {
            this.initSocketCache();
        }
        String flag = "randomId";
        String params = session.getQueryString();
        String randomId = WebUtil.requestString2Map(params).get(flag);
        if(randomId == null) {
            this.disconnect(session);
            return;
        }
        Long userId = socketCache.getUserId(randomId);
        System.out.println(userId);
//        connectCount.incrementAndGet();
//        userSession.put(uid,session);
//        System.out.println("open"+uid + "," + session.getId());
    }

    @Override
    @OnClose
    public void onClose(Session session) {
        userSession.values().remove(session);
        connectCount.decrementAndGet();
    }

    @Override
    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
    }

    @Override
    @OnError
    public void onError(Session session, Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void disconnect(Session session) {
        try {
            session.close();
        }catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void sendMessage(Session session, String message) {
        try {
            if(session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        }catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void sendGroupMessage(String message,@PathParam("uid") String uid) {
        if(uid != null) {
            Session session = userSession.get(uid);
            if(session == null) {
                return;
            }
            this.sendMessage(session,message);
            return;
        }
        userSession.keySet().forEach(id->this.sendMessage(userSession.get(id),message));
    }
}
