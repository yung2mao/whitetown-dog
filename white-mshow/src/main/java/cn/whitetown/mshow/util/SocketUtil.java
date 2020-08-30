package cn.whitetown.mshow.util;

import cn.whitetown.mshow.modo.ClientSession;

import javax.websocket.Session;
import javax.websocket.server.PathParam;
import java.io.IOException;

/**
 * @author taixian
 * @date 2020/08/30
 **/
public class SocketUtil {
    public static void sendMessage(ClientSession clientSession, String message) {
        if(clientSession == null) {
            return;
        }
        Session session = clientSession.getSession();
        try {
            if(session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void senGroupMessage() {}
}
