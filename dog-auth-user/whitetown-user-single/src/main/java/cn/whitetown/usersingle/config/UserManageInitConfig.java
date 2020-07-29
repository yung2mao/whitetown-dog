package cn.whitetown.usersingle.config;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.util.captcha.CaptchaDataDeal;
import cn.whitetown.authcommon.util.captcha.KapCaptchaDataDeal;
import cn.whitetown.authcommon.util.defaultimpl.WhiteJwtTokenUtil;
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
     * 初始化验证码配置
     * @return
     */
    @Bean
    public Producer getInstance(){
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "125");
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "35");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "28");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "5");
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
        return new KapCaptchaDataDeal();
    }

    /**
     * 初始化token配置
     * @return
     */
    @Bean
    public WhiteJwtTokenUtil jwtTokenUtil(){
        return new WhiteJwtTokenUtil(AuthConstant.TOKEN_SECRET,
                AuthConstant.TOKEN_EXPIRE,
                AuthConstant.TOKEN_PREFIX,
                AuthConstant.HEADER_STRING);
    }

}
