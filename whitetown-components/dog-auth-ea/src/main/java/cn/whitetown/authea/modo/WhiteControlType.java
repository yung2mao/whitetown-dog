package cn.whitetown.authea.modo;

/**
 * 权限控制类别
 * @author taixian
 * @date 2020/07/24
 **/
public class WhiteControlType {
    public static final String PERMIT_ALL = "PERMIT_ALL";
    public static final String AUTHENTICATED = "AUTHENTICATED";
    public static final String HAS_ANY_AUTHORITY = "HAS_ANY_AUTHORITY";
    public static final String HAS_AUTHORITY = "HAS_AUTHORITY";
    public static final String HAS_ANY_ROLE = "HAS_ANY_ROLE";
    public static final String HAS_ROLE = "HAS_ROLE";
    public static final String HAS_IP_ADDRESS = "HAS_IP_ADDRESS";
    public static final String NO_AUTH = "NO_AUTH";
}
