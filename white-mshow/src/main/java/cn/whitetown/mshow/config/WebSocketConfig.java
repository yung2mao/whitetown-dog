package cn.whitetown.mshow.config;

import cn.whitetown.dogbase.wache.WhiteExpireMap;
import cn.whitetown.mshow.manager.SocketCache;
import cn.whitetown.mshow.manager.wiml.DefaultSocketCache;
import cn.whitetown.mshow.service.wiml.WebSocketServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * websocket配置类
 * @author taixian
 * @date 2020/08/25
 **/
@Configuration
public class WebSocketConfig implements ApplicationRunner {

    public static final int CONNECT_PARAM_CACHE_TIME = 10;

    @Autowired
    private WhiteExpireMap expireMap;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 初始化websocket终端
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * websocket缓存工具
     * @return
     */
    @Bean
    public SocketCache socketCache() {
        return new DefaultSocketCache(expireMap);
    }

    @Override
    public void run(ApplicationArguments args) {
        WebSocketServerImpl.setApplicationContext(applicationContext);
    }
}
