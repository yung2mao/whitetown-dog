package cn.whitetown.mshow.config;

import cn.whitetown.dogbase.wache.WhiteExpireMap;
import cn.whitetown.dogbase.wache.wmil.SingleWhiteExpireMap;
import cn.whitetown.mshow.manager.SocketCache;
import cn.whitetown.mshow.manager.wiml.DefaultSocketCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author taixian
 * @date 2020/08/25
 **/
@Configuration
public class WebSocketConfig {

    public static final int CONNECT_PARAM_CACHE_TIME = 10;

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
        return new DefaultSocketCache(new SingleWhiteExpireMap());
    }

}
