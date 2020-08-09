package cn.whitetown.monitor.config;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.monitor.sys.anno.MonAnnotation;
import cn.whitetown.monitor.sys.anno.WhiteMonClient;
import cn.whitetown.monitor.sys.anno.WhiteMonServer;
import cn.whitetown.monitor.sys.client.SysMonClient;
import cn.whitetown.monitor.sys.client.SysMonitorRunner;
import cn.whitetown.monitor.sys.client.wmil.DefaultSysMonClient;
import cn.whitetown.monitor.sys.server.SysMonServer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 启动项
 * @author taixian
 * @date 2020/08/05
 **/
@Component
//@WhiteMonServer
//@WhiteMonClient
public class MonApplication implements ApplicationRunner {

    private Logger logger = LogConstants.sysLogger;

    @Autowired
    private ApplicationContext context;

    private SysMonClient sysMonClient;

    @Autowired
    @Qualifier(MonAnnotation.CLI_FILE_ONLY)
    @Lazy
    private SysMonitorRunner fileOnly;

    @Autowired
    @Qualifier(MonAnnotation.CLI_SENT_NOLY)
    @Lazy
    private SysMonitorRunner sentOnly;

    @Autowired
    @Qualifier(MonAnnotation.CLI_DEFAULT)
    @Lazy
    private SysMonitorRunner defaultClient;

    @Autowired
    @Lazy
    private SysMonServer sysMonServer;

    @Autowired
    @Lazy
    private ExecutorService executorService;

    private boolean isServer = false;

    private boolean isClient = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //server
        Map<String, Object> serverBeans = context.getBeansWithAnnotation(WhiteMonServer.class);
        if(serverBeans.size() != 0) {
            isServer = true;
            this.serverHandle(serverBeans);
        }

        //client
        Map<String, Object> clientBeans = context.getBeansWithAnnotation(WhiteMonClient.class);
        if(clientBeans.size() != 0) {
            isClient = true;
            this.clientHandle(clientBeans);
        }
    }

    @PreDestroy
    public void destroy() {
        if(isServer() && sysMonServer != null) { sysMonServer.destroy(); }
        if (isClient() && sysMonClient != null) { sysMonClient.destroy(); }
    }

    public void serverHandle(Map<String,Object> objectMap) {
        if(objectMap.size() == 0) {
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                sysMonServer.run();
                logger.info("the system monitor server is started");
            }
        });

    }

    /**
     * 客户端处理
     * @param objectMap
     */
    public void clientHandle(Map<String,Object> objectMap) {
        Iterator<Map.Entry<String, Object>> iterator = objectMap.entrySet().iterator();
        Object o = iterator.hasNext() ? iterator.next().getValue() : null;
        if(o == null) { return; }
        WhiteMonClient annotation = o.getClass().getDeclaredAnnotation(WhiteMonClient.class);
        String type = annotation.type();
        if(MonAnnotation.CLI_DEFAULT.equals(type)) {
            sysMonClient = DefaultSysMonClient.getInstance(defaultClient);
        }else if (MonAnnotation.CLI_FILE_ONLY.equals(type)) {
            sysMonClient = DefaultSysMonClient.getInstance(fileOnly);
        }else if (MonAnnotation.CLI_SENT_NOLY.equals(type)) {
            sysMonClient = DefaultSysMonClient.getInstance(sentOnly);
        }

        if(sysMonClient == null) {
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long beginTime = 60000;
                    Thread.sleep(beginTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sysMonClient.run();
                logger.info("the system monitor client is started");
            }
        });
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isClient() {
        return isClient;
    }
}
