package cn.whitetown.dogbase.common.util;

import cn.whitetown.dogbase.common.entity.vo.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.entity.ao.PageQuery;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 通用的处理各类一般事件的工具类
 * @author GrainRain
 * @date 2020/06/13 11:36
 **/
public class WhiteToolUtil {
    public WhiteToolUtil(){}

    /**
     * 随机生成一组字符串
     * @param len
     * @return
     */
    public static String createRandomString(int len){
        if(len<1){
            throw new IndexOutOfBoundsException("need >1, real "+len);
        }

        int start = '0';
        int end ='z';
        Random random = new Random();
        char[] textChar = new char[len];
        for (int i = 0; i < len; i++) {
            textChar[i] = (char)(random.nextInt(end-start)+start);
        }
        return String.valueOf(textChar);
    }

    /**
     * 两个对象的数据处理合并为一个对象的数据
     * 新对象中空值数据不保留
     * @param oldObj
     * @param newObj
     * @return
     */
    public static Object mergeObject(Object oldObj,Object newObj){
        if(oldObj.getClass() != newObj.getClass()){
            return newObj;
        }
        Field[] declaredFields = oldObj.getClass().getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        Arrays.stream(declaredFields).forEach(field -> fields.add(field));
        Class<?> superClass = oldObj.getClass().getSuperclass();
        while (superClass != null){
            Field[] superFields = superClass.getDeclaredFields();
            Arrays.stream(superFields).forEach(field -> fields.add(field));
            superClass = superClass.getSuperclass();
        }
        fields.stream().forEach(field -> {
            field.setAccessible(true);
            try {
                if(field.get(newObj) == null){
                    field.set(newObj,field.get(oldObj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return newObj;
    }

    /**
     * 获取md5密码
     * @param password
     * @param salt
     * @return
     */
    public static String md5Hex(String password, String salt) {
        return md5Hex(password + salt);
    }

    /**
     * 获取md5加密字符串
     * @param str
     * @return
     */
    public static String md5Hex(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(str.getBytes());
            StringBuffer md5StrBuff = new StringBuffer();

            for(int i = 0; i < bs.length; ++i) {
                if (Integer.toHexString(255 & bs[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(255 & bs[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(255 & bs[i]));
                }
            }

            return md5StrBuff.toString();
        } catch (Exception var5) {
            throw new CustomException(ResponseStatusEnum.ERROR_PARAMS);
        }
    }

    /**
     * 处理pageQuery，如果没有数据，赋默认值
     * @param pageQuery
     * @return
     */
    public static PageQuery defaultPage(PageQuery pageQuery){
        if(pageQuery==null){
            pageQuery = new PageQuery();
        }
        if(pageQuery.getPage()==null || pageQuery.getPage()<1){
            pageQuery.setPage(1);
        }
        if(pageQuery.getSize()==null || pageQuery.getSize()<1){
            pageQuery.setSize(10);
        }
        return pageQuery;
    }
}
