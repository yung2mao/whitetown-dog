package cn.whitetown.dogbase.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author GrainRain
 * @date 2020/05/24 11:37
 **/
public class FormatUtil {
    private FormatUtil(){}

    /**
     * 非空校验 - false抛出异常
     * @param str
     */
    public static void checkTextNull(String str){
        if(str==null || "".equals(str)){
            throw new NullPointerException("null text");
        }
    }

    /**
     * 非空校验
     * 如果为空返回true，否则返回false
     * @param str
     * @return
     */
    public static boolean checkTextNullBool(String str){
        if(str==null || "".equals(str)){
            return true;
        }
        return false;
    }

    /**
     * 将Date转化为毫秒值
     * @param date
     * @return
     */
    public static long timeAsLong(Date date){
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.getTimeInMillis();
    }
}
