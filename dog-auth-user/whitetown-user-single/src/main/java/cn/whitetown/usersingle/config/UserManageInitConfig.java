package cn.whitetown.usersingle.config;

import cn.whitetown.dogbase.user.captcha.CaptchaDataDeal;
import cn.whitetown.dogbase.user.captcha.SessionCaptchaDataDeal;
import cn.whitetown.dogbase.user.token.AuthConstant;
import cn.whitetown.dogbase.user.token.JwtTokenUtil;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 初始化配置类
 * @author GrainRain
 * @date 2020/05/24 17:36
 **/
@Configuration
public class UserManageInitConfig {
    /**
     * token密钥
     */
    private String tokenSecret = AuthConstant.TOKEN_SECRET;
    /**
     * token过期时间为一个月 - 一个月内免登录
     */
    private long tokenExpire = AuthConstant.TOKEN_EXPIRE;
    /**
     * 初始化验证码配置
     * @return
     */
    @Bean
    public Producer getInstance(){
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "100");
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "30");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "26");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "6");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "LIGHT_GRAY");
        properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM, "WHITE");
        properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        properties.setProperty(Constants.KAPTCHA_SESSION_CONFIG_KEY, "checkCode");
        Config config = new Config(properties);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }

    /**
     * 初始化验证码存取对象
     * @return
     */
    @Bean
    public CaptchaDataDeal getCaptchaDeal(){
        return new SessionCaptchaDataDeal();
    }

    /**
     * 初始化token配置
     * @return
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil(tokenSecret,tokenExpire);
    }

}
