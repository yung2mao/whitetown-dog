package cn.whitetown.dogbase.wache.excep;

/**
 * 元素清理异常
 * @author taixian
 * @date 2020/08/08
 **/
public class ElementRemoveException extends RuntimeException {
    public ElementRemoveException() {
    }

    public ElementRemoveException(String message) {
        super(message);
    }
}
