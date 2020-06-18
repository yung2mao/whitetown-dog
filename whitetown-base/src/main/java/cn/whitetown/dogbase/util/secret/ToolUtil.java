package cn.whitetown.dogbase.util.secret;


import cn.whitetown.dogbase.domain.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.exception.CustomException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public class ToolUtil extends ValidateUtil {
    public static final int SALT_LENGTH = 6;

    public ToolUtil() {
    }

    /**
     * 产生随机的字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    public static String removeWhiteSpace(String value) {
        return isEmpty(value) ? "" : value.replaceAll("\\s*", "");
    }

    public static String getCreateTimeBefore(int seconds) {
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date date = new Date(currentTimeInMillis - (long)(seconds * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getExceptionMsg(Throwable e) {
        StringWriter sw = new StringWriter();

        try {
            e.printStackTrace(new PrintWriter(sw));
        } finally {
            try {
                sw.close();
            } catch (IOException var8) {
                var8.printStackTrace();
            }

        }

        return sw.getBuffer().toString().replaceAll("\\$", "T");
    }


    public static String getIP() {
        try {
            StringBuilder IFCONFIG = new StringBuilder();
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        IFCONFIG.append(inetAddress.getHostAddress().toString() + "\n");
                    }
                }
            }

            return IFCONFIG.toString();
        } catch (SocketException var6) {
            var6.printStackTrace();

            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException var5) {
                var5.printStackTrace();
                return null;
            }
        }
    }

    public static Boolean isWinOs() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win") ? true : false;
    }

    public static String getTempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static Integer toInt(Object val) {
        if (val instanceof Double) {
            BigDecimal bigDecimal = new BigDecimal((Double)val);
            return bigDecimal.intValue();
        } else {
            return Integer.valueOf(val.toString());
        }
    }

    public static boolean isNum(Object obj) {
        try {
            Integer.parseInt(obj.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static String getWebRootPath(String filePath) {
        try {
            String path = ToolUtil.class.getClassLoader().getResource("").toURI().getPath();
            path = path.replace("/WEB-INF/classes/", "");
            path = path.replace("/target/classes/", "");
            path = path.replace("file:/", "");
            return isEmpty(filePath) ? path : path + "/" + filePath;
        } catch (URISyntaxException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String getFileSuffix(String fileWholeName) {
        if (isEmpty(fileWholeName)) {
            return "none";
        } else {
            int lastIndexOf = fileWholeName.lastIndexOf(".");
            return fileWholeName.substring(lastIndexOf + 1);
        }
    }

    public static String stringReplaceBuild(String value, Map<String, Object> params) {
        String result = value;
        Iterator var3 = params.keySet().iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            if (params.get(key) != null) {
                result = result.replace("#{" + key + "}", params.get(key).toString());
            }
        }

        return result;
    }
}
