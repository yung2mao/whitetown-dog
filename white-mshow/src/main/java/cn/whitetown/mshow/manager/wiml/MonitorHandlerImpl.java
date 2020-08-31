package cn.whitetown.mshow.manager.wiml;

import cn.hutool.core.thread.ThreadUtil;
import cn.whitetown.monitor.sys.modo.dto.WhiteMonitorParams;
import cn.whitetown.monitor.sys.server.wiml.CacheSaveManager;
import cn.whitetown.monitor.util.DestroyHook;
import cn.whitetown.mshow.config.SysMonitorConfig;
import cn.whitetown.mshow.manager.MonitorHandler;
import cn.whitetown.mshow.modo.ClientSession;
import cn.whitetown.mshow.modo.SessionTypeGroup;
import cn.whitetown.mshow.util.SocketUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 监控信息监听处理实现
 * @author taixian
 * @date 2020/08/30
 **/
@Component
public class MonitorHandlerImpl implements MonitorHandler {

    @Autowired
    @Lazy
    private CacheSaveManager monCacheManager;

    @Autowired
    private volatile SessionTypeGroup sessionGroup;

    @Autowired
    private ExecutorService executorService;

    private volatile boolean isListen = true;

    public MonitorHandlerImpl() {
        DestroyHook.destroy(this::destroy);
    }

    @PostConstruct
    @Override
    public void listener() {
        executorService.execute(new MonHandle());
    }

    @Override
    public void analyzer(WhiteMonitorParams monitorParams) {
    }

    @Override
    public void sendMessage(ClientSession session) {
        String serverId = session.getGroupId().replace(SysMonitorConfig.GROUP_PREFIX, "");
        boolean firstConnect = session.isFirstConnect();
        if(firstConnect) {
            List<WhiteMonitorParams> allParams = monCacheManager.getAll(serverId);
            System.out.println(allParams);
            session.setFirstConnect(false);
            return;
        }
        WhiteMonitorParams recentParams = monCacheManager.getRecent(serverId);
        SocketUtil.sendMessage(session, JSON.toJSONString(recentParams));
    }

    public void destroy() {
        isListen = false;
    }

    /*------------------task class--------------------*/

    private class MonHandle implements Runnable {
        @Override
        public void run() {
            long waitTime = 10;
            ThreadUtil.sleep(waitTime,TimeUnit.SECONDS);
            while (isListen) {
                Map<String, List<ClientSession>> serverGroup = MonitorHandlerImpl.this.sessionGroup.filterSessions(SysMonitorConfig.GROUP_PREFIX);
                Set<String> groupIds = serverGroup.keySet();
                groupIds.forEach(groupId -> {
                    List<ClientSession> clientSessions = serverGroup.get(groupId);
                    clientSessions.forEach(MonitorHandlerImpl.this::sendMessage);
                });
                long intervalTime = 3000;
                ThreadUtil.sleep(intervalTime, TimeUnit.MILLISECONDS);
            }
        }
    }
}
