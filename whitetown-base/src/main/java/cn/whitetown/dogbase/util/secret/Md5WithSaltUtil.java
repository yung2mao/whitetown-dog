package cn.whitetown.dogbase.util.secret;


import cn.whitetown.dogbase.util.WhiteToolUtil;

/**
 * MD5加盐工具类
 * @author taixian
 */
public class Md5WithSaltUtil {

    private Md5WithSaltUtil(){}

    /**
     * 获取密码盐
     * @return
     */
    public static String getRandomSalt() {
        return WhiteToolUtil.createRandomString(7);
    }

    /**
     * md5加密，带盐值
     * @param password
     * @param salt
     * @return
     */
    public static String md5Encrypt(String password, String salt) {
        if (ToolUtil.isOneEmpty(password, salt)) {
            throw new IllegalArgumentException("密码或盐为空！");
        } else {
            return MD5Util.encrypt(password + salt);
        }
    }
}
