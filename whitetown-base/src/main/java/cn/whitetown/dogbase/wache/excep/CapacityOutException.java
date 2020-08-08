package cn.whitetown.dogbase.wache.excep;

/**
 * 容量溢出异常
 * @author taixian
 * @date 2020/08/08
 **/
public class CapacityOutException extends RuntimeException {

    public CapacityOutException() {
        super("the capacity has reached the upper limit , there are no elements to remove");
    }

    public CapacityOutException(String message) {
        super(message);
    }
}
