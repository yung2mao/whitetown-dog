package cn.whitetown.monitor.config;

import cn.whitetown.monitor.sys.client.SysMonClient;
import cn.whitetown.monitor.sys.server.SysMonServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 启动项
 * @author taixian
 * @date 2020/08/05
 **/
@Component
public class MonApplication implements ApplicationRunner {

    @Autowired
    @Lazy
    private SysMonClient sysMonClient;

    @Autowired
    @Lazy
    private SysMonServer sysMonServer;

    @Autowired
    @Lazy
    private ExecutorService executorService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                sysMonServer.run();
            }
        });
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
            }
        });
    }

    @PreDestroy
    public void destroy() {
        sysMonClient.destroy();
        sysMonServer.destroy();
    }
}
