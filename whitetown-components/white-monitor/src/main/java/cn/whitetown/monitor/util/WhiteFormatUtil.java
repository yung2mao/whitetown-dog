package cn.whitetown.monitor.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 格式化相关工具类
 * 封装了多种数据转换格式化方法
 * @author GrainRain
 * @date 2020/05/24 11:37
 **/
public class WhiteFormatUtil {
    private WhiteFormatUtil(){}

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

    /**
     * 毫秒转Date
     * @param time
     * @return
     */
    public static Date millisToDate(Long time) {
        if(time == null) {
            return new Date();
        }
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(time);
        return ca.getTime();
    }

    /**
     * 日期转为指定格式String
     * @param format
     * @param date
     * @return
     */
    public static String dateFormat(String format,Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 默认转换
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return dateFormat(format,date);
    }

    /**
     * 时间范围转换 - 秒/分钟/小时/天/月/年
     * @param time
     * @param sourceTimeUnit
     * @param targetTimeUnit
     * @return
     */
    public static String timeScopeFormat(long time, TimeUnit sourceTimeUnit, TimeUnit targetTimeUnit) {
        if(targetTimeUnit == null) {
            int minutes = (int)sourceTimeUnit.toMinutes(time);
            int hours = (int)sourceTimeUnit.toHours(time);
            int days = (int)sourceTimeUnit.toDays(time);
            int month = days / 30;
            int year = days / 365;
            if(year > 0) {
                int left = month % 12;
                return year + "年 " + (left > 0 ? left + "月" : "");  }
            else if(month > 0) {
                int left = days % 30;
                return month + "月 " + (left > 0 ? left + "天" : "");
            }
            else if(days > 0) {
                int left = hours % 24;
                return days + "天 " + (left > 0 ? left + "小时" : "");
            }
            else if (hours > 0) {
                int left = minutes % 60;
                return hours + "小时 " + (left > 0 ? left + "分钟" : "");
            }else {
                long seconds = sourceTimeUnit.toSeconds(time);
                int left = (int) (seconds % 60);
                return minutes + "分钟 " + (left > 0 ? left + "秒" : "");
            }
        }

        switch (targetTimeUnit) {
            case NANOSECONDS:return sourceTimeUnit.toNanos(time) + "纳秒";
            case MICROSECONDS:return sourceTimeUnit.toMicros(time) + "微妙";
            case MILLISECONDS:return sourceTimeUnit.toMillis(time) + "毫秒";
            case SECONDS:return sourceTimeUnit.toSeconds(time) + "秒";
            case MINUTES:return sourceTimeUnit.toMinutes(time) + "分钟";
            case HOURS:return sourceTimeUnit.toHours(time) + "小时";
            default:
                return sourceTimeUnit.toDays(time) + "天";
        }
    }
}
