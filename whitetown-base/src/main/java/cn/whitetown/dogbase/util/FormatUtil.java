package cn.whitetown.dogbase.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 格式化相关工具类
 * 封装了多种数据转换格式化方法
 * @author GrainRain
 * @date 2020/05/24 11:37
 **/
public class FormatUtil {
    private FormatUtil(){}

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
