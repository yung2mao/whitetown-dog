package cn.whitetown.authcommon.util.captcha;

import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.authcommon.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 默认的验证码生成与处理实现
 * @author GrainRain
 * @date 2020/06/13 10:44
 **/
public class DefaultCaptchaDataDeal implements CaptchaDataDeal {
    /**
     * 用于生成随机数
     */
    private Random random = new Random();
    /**
     * 验证码的位数
     */
    private int length;

    /**
     * 验证码图片的宽
     */
    private int width;
    /**
     * 验证码图片的高
     */
    private int height;
    /**
     * 字体大小
     */
    private int fontSize;
    /**
     * 验证码的字符信息
     */
    private char[] defaultArr;

    @Autowired
    private WhiteExpireMap expireMap;

    public DefaultCaptchaDataDeal(CaptchaBasicInfo basicInfo) {
        this.length = basicInfo.getLength();
        this.width = basicInfo.getWidth();
        this.height = basicInfo.getHeight();
        this.fontSize = basicInfo.getFontSize();
        this.defaultArr = basicInfo.getDefaultArr();
    }

    /**
     * 随机获取颜色
     * @return
     */
    private Color getRandomColor() {
        //随机生成0-255之间整数
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        //参数：红，绿，蓝 (0-255)
        return new Color(red, green, blue);
    }

    @Override
    public String createCaptchaText() {
        char[] captchaArr = new char[length];
        for (int i = 0; i < length; i++) {
            int randIndex = random.nextInt(defaultArr.length);
            captchaArr[i] = defaultArr[randIndex];
        }
        return String.valueOf(captchaArr);
    }

    @Override
    public BufferedImage createCaptchaImage(String text) {
        //参数：宽，高，图片模式
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = img.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        char[] charText = text.toCharArray();
        for (int i = 0; i < length; i++) {
            graphics.setColor(getRandomColor());
            //字体，样式，大小
            graphics.setFont(new Font(Font.DIALOG, Font.BOLD + Font.ITALIC, fontSize));
            graphics.drawString(String.valueOf(charText[i]), 10 + (i * 20), 20);
        }
        for (int i = 0; i < 8; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            graphics.setColor(getRandomColor());
            graphics.drawLine(x1, y1, x2, y2);
        }
        return img;
    }

    /**
     * 验证码临时存储
     * @param sessionId
     * @param captchaText
     */
    @Override
    public void saveCaptcha(String sessionId, String captchaText) {
        expireMap.putS(sessionId, captchaText,AuthConstant.CAPTCHA_EXPIRE_TIME);
    }

    /**
     * 获取内存中存储的验证码信息
     * @param sessionId
     * @return
     */
    @Override
    public String getCaptcha(String sessionId) {
        return (String) expireMap.get(sessionId);
    }

}
