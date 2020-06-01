package cn.whitetown.dogbase.user.captcha;

import java.awt.image.BufferedImage;

/**
 * 验证码相关操作
 * @author GrainRain
 * @date 2020/05/26 22:35
 **/
public interface CaptchaDataDeal {
    /**
     * 创建验证码字符串
     * @return
     */
    public String createCaptchaText();

    /**
     * 创建验证码图片
     * @param text
     * @return
     */
    public BufferedImage createCaptchaImage(String text);

    /**
     * 暂存验证码
     * @param sessionId
     * @param captchaText
     */
    public void saveCaptcha(String sessionId, String captchaText);

    /**
     * 获取验证码
     * @param sessionId
     * @return
     */
    public String getCaptcha(String sessionId);
}
