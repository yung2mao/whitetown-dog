package cn.whitetown.authcommon.util.captcha;

/**
 * 验证码相关的基本信息
 * @author GrainRain
 * @date 2020/06/13 11:19
 **/
public class CaptchaBasicInfo {
    /**
     * 验证码的位数
     */
    private int length = 4;

    /**
     * 验证码图片的宽
     */
    private int width = 100;
    /**
     * 验证码图片的高
     */
    private int height = 30;
    /**
     * 字体大小
     */
    private int fontSize = 20;
    /**
     * 验证码的字符信息
     */
    private char[] defaultArr = {'A', 'B', 'C', 'D', 'N', 'E', 'W', 'b', 'o', 'y','l','c','q',
            '0', '1', '2', '3', '4', '5', '6','7','8','9'};

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public char[] getDefaultArr() {
        return defaultArr;
    }

    public void setDefaultArr(char[] defaultArr) {
        this.defaultArr = defaultArr;
    }
}
