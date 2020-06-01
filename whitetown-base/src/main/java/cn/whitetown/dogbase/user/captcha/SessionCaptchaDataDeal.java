package cn.whitetown.dogbase.user.captcha;

import cn.whitetown.dogbase.domain.special.WhiteExpireMap;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * @author GrainRain
 * @date 2020/05/26 22:40
 **/
public class SessionCaptchaDataDeal implements CaptchaDataDeal {

    @Autowired
    private WhiteExpireMap whiteExpireMap;
    @Autowired
    private Producer producer;

    @Override
    public String createCaptchaText() {
        return producer.createText();
    }

    @Override
    public BufferedImage createCaptchaImage(String capText) {
        return producer.createImage(capText);
    }

    /**
     * 将验证码存入内存中并设定过期时间
     * @param sessionId
     * @param captchaText
     */
    @Override
    public void saveCaptcha(String sessionId, String captchaText) {
        whiteExpireMap.put(sessionId+"captcha",captchaText,180000);
    }

    /**
     * 获取验证码
     * @param sessionId
     * @return
     */
    @Override
    public String getCaptcha(String sessionId) {
        return (String)whiteExpireMap.get(sessionId+"captcha");
    }

    /**
     * 获取session
     * @return
     */
    private HttpSession getSession(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        return session;
    }
}
