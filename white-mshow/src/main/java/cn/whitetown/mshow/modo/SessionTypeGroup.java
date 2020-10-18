package cn.whitetown.mshow.modo;

import cn.whitetown.dogbase.common.util.WriteSyncList;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket session对应的操作id
 * @author taixian
 * @date 2020/08/30
 **/
@Component
public class SessionTypeGroup {

    private Map<String, List<ClientSession>> typeAndSession;

    private Map<ClientSession, String> sessionWithType;

    public SessionTypeGroup() {
        this.typeAndSession = new ConcurrentHashMap<>();
        sessionWithType = new ConcurrentHashMap<>();
    }

    /**
     * session添加到组
     * @param groupId 组ID
     * @param clientSession 连接的客户端session
     */
    public void session2Group(String groupId, ClientSession clientSession) {
        this.removeSession(clientSession);
        List<ClientSession> groupSession = typeAndSession.get(groupId);
        if(groupSession == null) {
            groupSession = new WriteSyncList<>(new ArrayList<>());
            typeAndSession.put(groupId,groupSession);
        }
        clientSession.setGroupId(groupId);
        sessionWithType.put(clientSession,groupId);
        typeAndSession.get(groupId).add(clientSession);
    }

    /**
     * 移除分组中的session信息
     * @param clientSession
     */
    public void removeSession(ClientSession clientSession) {
        String groupId = sessionWithType.remove(clientSession);
        if(groupId == null) {
            return;
        }
        clientSession.setGroupId(null);
        typeAndSession.get(groupId).remove(clientSession);
    }

    /**
     * 获取所有组session
     * @return
     */
    public Map<String,List<ClientSession>> getAll() {
        return typeAndSession;
    }

    /**
     * 获取分组中所有session信息
     * @param groupId 组ID
     * @return
     */
    public List<ClientSession> getGroupSessions(String groupId) {
        return typeAndSession.get(groupId);
    }

    public Map<String,List<ClientSession>> filterSessions(String prefix) {
        Map<String, List<ClientSession>> map = new HashMap<>(2);
        Set<String> keys = typeAndSession.keySet();
        keys.forEach(key->{
            if(key.startsWith(prefix)) {
                String serverId = key.replace(prefix, "");
                map.put(serverId,typeAndSession.get(key));
            }
        });
        return map;
    }
}
