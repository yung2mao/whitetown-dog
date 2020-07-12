package cn.whitetown.usersecurity.config;

import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.authcommon.util.MenuUtil;
import cn.whitetown.authcommon.util.UserCacheUtil;
import cn.whitetown.authcommon.util.captcha.CaptchaBasicInfo;
import cn.whitetown.authcommon.util.captcha.CaptchaDataDeal;
import cn.whitetown.authcommon.util.captcha.DefaultCaptchaDataDeal;
import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.util.defaultimpl.DefaultMenuCacheUtil;
import cn.whitetown.authcommon.util.defaultimpl.DefaultMenuUtil;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.authcommon.util.defaultimpl.DefaultUserCacheUtil;
import cn.whitetown.usersecurity.util.securityHandler.AccessFailHandler;
import cn.whitetown.usersecurity.util.securityHandler.AuthenticationErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author GrainRain
 * @date 2020/06/13 11:30
 **/
@Configuration
public class UserInitConfig {
    /**
     * token密钥
     */
    private String tokenSecret = AuthConstant.TOKEN_SECRET;
    /**
     * token过期时间为7天 - 7天内免登录
     */
    private long tokenExpire = AuthConstant.TOKEN_EXPIRE;

    /**
     * 初始化验证码操作类
     * @return
     */
    @Bean
    public CaptchaDataDeal defaultCaptchaDataDeal(){
        return new DefaultCaptchaDataDeal(new CaptchaBasicInfo());
    }

    /**
     * 初始化token配置
     * @return
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil(tokenSecret,tokenExpire);
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

    /**
     * 初始化用户信息缓存工具类
     * @return
     */
    @Bean
    public UserCacheUtil userCacheUtil(){
        return new DefaultUserCacheUtil();
    }

    /**
     * 初始化菜单管理工具类
     * @return
     */
    @Bean
    public MenuUtil menuUtil(){
        return new DefaultMenuUtil();
    }

    /**
     * 初始化菜单缓存工具
     * @return
     */
    @Bean
    public MenuCacheUtil menuCacheUtil(){
        DefaultMenuCacheUtil menuCacheUtil = new DefaultMenuCacheUtil();
        menuCacheUtil.init();
        return menuCacheUtil;
    }
}
