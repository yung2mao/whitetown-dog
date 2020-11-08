package cn.whitetown.event.modo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 队列元数据Map
 * group - <name - queue>
 * @Author: taixian
 * @Date: created in 2020/10/26
 */
public class QueueMap {

    private Map<String, Map<String, QueueMeta>> channelMetaMap;

    private final int initCapacity = 8;

    public QueueMap(Map<String, Map<String, QueueMeta>> channelMetaMap) {
        this.channelMetaMap = channelMetaMap;
    }

    public QueueMap() {

        channelMetaMap = new ConcurrentHashMap<>(initCapacity);
    }

    public void put(String groupId, QueueMeta queueMeta) {
        Map<String, QueueMeta> groupMap = channelMetaMap.get(groupId);
        if(groupMap == null) {
            groupMap = new ConcurrentHashMap<>(initCapacity);
            channelMetaMap.put(groupId,groupMap);
        }
        groupMap.put(queueMeta.getName(), queueMeta);
    }

    public QueueMeta getChannel(String groupId, String name) {
        Map<String, QueueMeta> groupMap = channelMetaMap.get(groupId);
        return groupMap == null ? null : groupMap.get(name);
    }
}
