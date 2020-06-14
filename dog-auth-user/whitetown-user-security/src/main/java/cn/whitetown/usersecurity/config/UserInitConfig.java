package cn.whitetown.usersecurity.config;

import cn.whitetown.dogbase.user.captcha.CaptchaBasicInfo;
import cn.whitetown.dogbase.user.captcha.DefaultCaptchaDataDeal;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import cn.whitetown.usersecurity.util.securityHandler.AccessFailHandler;
import cn.whitetown.usersecurity.util.securityHandler.NotLoginHandler;
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
    public DefaultCaptchaDataDeal defaultCaptchaDataDeal(){
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
    public NotLoginHandler notLoginHandler(){
        return new NotLoginHandler();
    }
}
