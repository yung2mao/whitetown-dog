package cn.whitetown.dogbase.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * 线程池配置
 * @author taixian
 * @date 2020/07/31
 **/
@Configuration
public class WhiteThreadPoolConfig {

    public static int poolMaxSize = 4;

    @Autowired
    private ThreadPoolMap threadPoolMap;

    @Bean
    public ThreadPoolMap threadPoolMap() {
        return new ThreadPoolMap(poolMaxSize,new HashMap<>(4));
    }

    /**
     * 初始化线程池
     * @return
     */
    @Bean("white_component_pool")
    public ExecutorService executorService() {
        int corePoolSize = 8;
        int maxPoolSize = 32;
        int keepAliveTime = 60;
        String threadPoolName = "white_component_pool";
        CustomizableThreadFactory threadFactory = new CustomizableThreadFactory();
        threadFactory.setThreadGroupName(threadPoolName);
        BlockingQueue theadQueue = new ArrayBlockingQueue(1024);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                theadQueue,
                threadFactory);
        threadPoolMap.putThreadPool(threadPoolName,threadPoolExecutor);
        return threadPoolExecutor;
    }
}
