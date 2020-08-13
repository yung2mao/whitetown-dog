package cn.whitetown.dogbase.common.util;

/**
 * @author taixian
 * @date 2020/04/21
 **/
public class BaseIDCreateUtil {

    private BaseIDCreateUtil(){}

    private static int serialNumber = 10000+(int)(1000 * Math.random());

    public static String getId(String businessCode){
        Long time = System.currentTimeMillis();
        synchronized (BaseIDCreateUtil.class){
            serialNumber += 1;
            if(serialNumber > 99999) {
                serialNumber = 10000 + (int) (1000 * Math.random());
            }
            return time.toString() + serialNumber + businessCode;
        }
    }
    public static String getId(){
        Long time = System.currentTimeMillis();
        synchronized (BaseIDCreateUtil.class){
            serialNumber += 1;
            if(serialNumber > 99999) {
                serialNumber = 10000 + (int) (1000 * Math.random());
            }
            return time.toString() + serialNumber;
        }
    }

}
