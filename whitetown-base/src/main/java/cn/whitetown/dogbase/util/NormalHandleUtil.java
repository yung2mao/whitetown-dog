package cn.whitetown.dogbase.util;

import org.junit.Test;

import java.util.Random;

/**
 * 通用的处理各类一般事件的工具类
 * @author GrainRain
 * @date 2020/06/13 11:36
 **/
public class NormalHandleUtil {
    public NormalHandleUtil(){}

    /**
     * 随机生成一组字符串
     * @param len
     * @return
     */
    public static String createRandom(int len){
        if(len<1){
            throw new IndexOutOfBoundsException("need >1, real "+len);
        }

        int start = '0';
        int end ='z';
        Random random = new Random();
        char[] textChar = new char[len];
        for (int i = 0; i < len; i++) {
            textChar[i] = (char)(random.nextInt(end-start)+start);
        }
        return String.valueOf(textChar);
    }
}
