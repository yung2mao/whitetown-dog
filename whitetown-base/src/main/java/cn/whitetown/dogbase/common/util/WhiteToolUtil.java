package cn.whitetown.dogbase.common.util;

import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.entity.ao.PageQuery;
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

    /**
     * 结果集分页处理
     * @param resultList
     * @param page
     * @param size
     * @param <T>
     * @return
     */
    public static <T> ResponsePage<T> result2Page(List<T> resultList, Integer page, Integer size) {
        if(page == null || size == null || resultList == null){
            throw new NullPointerException("page , size or result is null");
        }
        ResponsePage<T> responsePage = new ResponsePage<>();
        Integer startRow = (page-1) * size;
        Integer endRow = page * size;
        if(startRow < 0 || startRow > resultList.size()){
            return responsePage;
        }
        for (int i = startRow; i < resultList.size(); i++) {
            responsePage.getResultList().add(resultList.get(i));
            if(i >= endRow){
                break;
            }
        }
        responsePage.setPage(Long.valueOf(page));
        responsePage.setRows(Long.valueOf(size));
        responsePage.setTotal(Long.valueOf(resultList.size()));
        return responsePage;
    }

    /**
     * 从entity获取指定属性值
     * @param fieldName
     * @param entity
     * @return
     */
    public static Object getFieldValue(String fieldName,Object entity) {
        if(fieldName == null || entity == null) {
            return null;
        }
        String text = JSON.toJSONString(entity);
        Map map = JSON.parseObject(text, Map.class);
        return map.get(fieldName);
    }

    /**
     * 根据传入函数获取属性名称 - 函数方法名必须get开头
     * @param column
     * @param <R>
     * @return
     */
    public static <R> String getFieldName(WhiteFunc<R,Object> column) {
        String methodName = getMethodByLambda(column);
        String methodPrefix = "get";
        int minLen = 4;
        if(methodName == null || !methodName.startsWith(methodPrefix) || methodName.length() < minLen) {
            return null;
        }
        methodName = methodName.replace(methodPrefix, "");
        char first = methodName.charAt(0);
        return methodName.replace(first,Character.toLowerCase(first));
    }

    /**
     * 获取传入函数的方法名
     * @param column
     * @param <T>
     * @return
     */
    public static  <T> String getMethodByLambda(WhiteFunc<T,Object> column) {
        try {
            String  reflectName = "writeReplace";
            Method writeReplace = column.getClass().getDeclaredMethod(reflectName);
            writeReplace.setAccessible(true);
            Object invoke = writeReplace.invoke(column);
            SerializedLambda serializedLambda = (SerializedLambda) invoke;
            return serializedLambda.getImplMethodName();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
