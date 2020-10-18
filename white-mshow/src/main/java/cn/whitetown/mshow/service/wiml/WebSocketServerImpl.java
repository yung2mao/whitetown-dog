package cn.whitetown.mshow.service.wiml;

import cn.whitetown.dogbase.common.util.WebUtil;
import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.mshow.manager.SocketCache;
import cn.whitetown.mshow.modo.ClientSession;
import cn.whitetown.mshow.modo.SessionMap;
import cn.whitetown.mshow.modo.SessionTypeGroup;
import cn.whitetown.mshow.service.WebSocketServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
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

    private Log log = LogFactory.getLog(WebSocketServerImpl.class);

    private static AtomicInteger connectCount = new AtomicInteger(0);

    private SessionMap sessionMap = SessionMap.sessionMap;

    /**
     * websocket是多例模型,不能直接注入
     */
    private SocketCache socketCache;
    private SessionTypeGroup sessionTypeGroup;

    private static ApplicationContext applicationContext;

    /**
     * 引入spring的上下文对象
     * @param context  spring上下文对象
     */
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    private void initSpringBean() {
        socketCache = applicationContext.getBean(SocketCache.class);
        sessionTypeGroup = applicationContext.getBean(SessionTypeGroup.class);
    }

    @Override
    @OnOpen
    public void onOpen(Session session) {
        if(socketCache == null) {
            this.initSpringBean();
        }
        //get identity
        String params = session.getQueryString();
        Map<String, String> queryMap = WebUtil.requestString2Map(params);
        String randomId = queryMap.get("randomId");
        String usId = queryMap.get("userId");
        String groupId = queryMap.get("groupId");
        Long userId = usId == null ? -1 : Long.parseLong(usId);
        //compare identity
        Long realUserId = socketCache.getUserId(randomId);
        if(randomId == null || !realUserId.equals(userId)) {
            this.disconnect(session);
            return;
        }
        connectCount.incrementAndGet();
        ClientSession clientSession = new ClientSession();
        clientSession.setClientId(userId + "");
        clientSession.setSession(session);
        //cache session
        sessionMap.put(userId + "", clientSession);
        //add to group
        sessionTypeGroup.session2Group(groupId,clientSession);
    }

    @Override
    @OnClose
    public void onClose(Session session) {
        ClientSession clientSession = sessionMap.remove(session);
        sessionTypeGroup.removeSession(clientSession);
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
        this.disconnect(session);
        log.error(cause.getMessage());
    }

    @Override
    public void disconnect(Session session) {
        try {
            session.close();
            this.onClose(session);
        }catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
