package cn.whitetown.authcommon.util.captcha;

import cn.whitetown.authcommon.util.defaultimpl.WhiteUserCacheUtil;
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
public class KapCaptchaDataDeal implements CaptchaDataDeal {

    @Autowired
    private WhiteUserCacheUtil userCacheUtil;

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
        userCacheUtil.saveCaptcha(sessionId,captchaText);
    }

    /**
     * 从内存中获取获取验证码
     * @param sessionId
     * @return
     */
    @Override
    public String getCaptcha(String sessionId) {
        return userCacheUtil.getCaptcha(sessionId);
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
