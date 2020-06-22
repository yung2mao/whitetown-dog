package cn.whitetown.dogbase.common.util;

/**
 * 数据校验工具类
 * @author GrainRain
 * @date 2020/06/13 14:21
 **/
public class DataCheckUtil {

    private DataCheckUtil(){}
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
}
