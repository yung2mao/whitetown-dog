package cn.whitetown.mshow.modo;

import cn.whitetown.monitor.util.DestroyHook;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket存储session信息map
 * @author taixian
 * @date 2020/08/30
 **/
public final class SessionMap {

    public static SessionMap sessionMap = new SessionMap();

    private static Map<String, ClientSession> userSession = new ConcurrentHashMap<>(2);
    private static Map<Session, String> sessionUser = new ConcurrentHashMap<>(2);

    private SessionMap() {
        DestroyHook.destroy(this::clear);
    }

    public void put(String id, ClientSession clientSession) {
        userSession.put(id + "", clientSession);
        sessionUser.put(clientSession.getSession(), id + "");
    }

    public ClientSession get(String id) {
        return userSession.get(id);
    }

    public ClientSession remove(String id) {
        ClientSession clientSession = userSession.remove(id);
        if(clientSession == null) {
            return null;
        }
        sessionUser.remove(clientSession.getClientId());
        return clientSession;
    }

    public ClientSession remove(Session session) {
        try {
            String id = sessionUser.remove(session);
            return userSession.remove(id);
        }catch (Exception ignored) {
            return null;
        }
    }

    public void clear() {
        userSession.clear();
        sessionUser.clear();
    }
}
