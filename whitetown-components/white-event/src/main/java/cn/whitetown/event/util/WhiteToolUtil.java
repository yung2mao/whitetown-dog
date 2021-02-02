package cn.whitetown.event.util;

import com.alibaba.fastjson.JSON;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.*;

/**
 * 通用的处理各类一般事件的工具类
 * @author GrainRain
 * @date 2020/06/13 11:36
 **/
public class WhiteToolUtil {
    public WhiteToolUtil() {
    }

    /**
     * 随机生成一组字符串
     *
     * @param len
     * @return
     */
    public static String createRandomString(int len) {
        if (len < 1) {
            throw new IndexOutOfBoundsException("need >1, real " + len);
        }

        int start = '0';
        int end = 'z';
        Random random = new Random();
        char[] textChar = new char[len];
        for (int i = 0; i < len; i++) {
            textChar[i] = (char) (random.nextInt(end - start) + start);
        }
        return String.valueOf(textChar);
    }

    /**
     * 两个对象的数据处理合并为一个对象的数据
     * 新对象中空值数据不保留
     *
     * @param oldObj
     * @param newObj
     * @return
     */
    public static Object mergeObject(Object oldObj, Object newObj) {
        if (oldObj.getClass() != newObj.getClass()) {
            return newObj;
        }
        Field[] declaredFields = oldObj.getClass().getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        Arrays.stream(declaredFields).forEach(field -> fields.add(field));
        Class<?> superClass = oldObj.getClass().getSuperclass();
        while (superClass != null) {
            Field[] superFields = superClass.getDeclaredFields();
            Arrays.stream(superFields).forEach(field -> fields.add(field));
            superClass = superClass.getSuperclass();
        }
        fields.stream().forEach(field -> {
            field.setAccessible(true);
            try {
                if (field.get(newObj) == null) {
                    field.set(newObj, field.get(oldObj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return newObj;
    }

    /**
     * 从entity获取指定属性值
     *
     * @param fieldName
     * @param entity
     * @return
     */
    public static Object getFieldValue(String fieldName, Object entity) {
        if (fieldName == null || entity == null) {
            return null;
        }
        String text = JSON.toJSONString(entity);
        Map map = JSON.parseObject(text, Map.class);
        return map.get(fieldName);
    }
}
