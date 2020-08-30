package cn.whitetown.mshow.modo;

import lombok.Getter;
import lombok.Setter;

import javax.websocket.Session;

/**
 * websocket客户端信息
 * @author taixian
 * @date 2020/08/30
 **/
@Setter
@Getter
public class ClientSession {
    private String clientId;
    private String groupId;
    private Session session;
    private boolean isFirstConnect = true;
    private boolean isConnect = true;
}
