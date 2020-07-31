package cn.whitetown.authea.config;

import cn.whitetown.authea.manager.SpringSecurityConfigureManager;
import cn.whitetown.authea.manager.WhiteSecurityConfigureManager;
import cn.whitetown.authea.manager.refuse.AccessFailHandler;
import cn.whitetown.authea.manager.refuse.AuthenticationErrorHandler;
import cn.whitetown.authea.util.WhiteAuthCacheUtil;
import cn.whitetown.authea.util.AuthCacheUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author taixian
 * @date 2020/07/27
 **/
@Configuration
public class WhiteAuthInitConfig {

    /**
     * 初始化权限控制缓存工具类
     * @return
     */
    @Bean
    public AuthCacheUtil authCacheUtil(){
        return new WhiteAuthCacheUtil();
    }

    /**
     * 初始化security管理类
     * @return
     */
    @Bean
    public SpringSecurityConfigureManager springSecurityConfigureManager() {
        return new WhiteSecurityConfigureManager(new HashMap<>(16),
                new HashMap<>(16));
    }

    /**
     * 初始化权限异常处理器
     * @return
     */
    @Bean
    public AccessFailHandler accessFailHandler(){
        return new AccessFailHandler();
    }

    /**
     * 初始化未登录用户处理器
     * @return
     */
    @Bean
    public AuthenticationErrorHandler notLoginHandler(){
        return new AuthenticationErrorHandler();
    }
}
