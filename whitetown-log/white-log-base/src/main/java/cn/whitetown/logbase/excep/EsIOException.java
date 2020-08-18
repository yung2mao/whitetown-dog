package cn.whitetown.logbase.excep;

/**
 * @author taixian
 * @date 2020/08/18
 **/
public class EsIOException extends RuntimeException {
    public EsIOException() {
    }

    public EsIOException(String message) {
        super(message);
    }
}
