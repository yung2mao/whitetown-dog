package cn.whitetown.esconfig.utils;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.esconfig.annotation.EsFieldConfig;
import cn.whitetown.esconfig.annotation.IndexIgnore;
import cn.whitetown.esconfig.config.EsConfigEnum;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author taixian
 * @date 2020/08/16
 **/
public class EsTools {
    private EsTools(){}

    public static final EsTools ES_TOOLS = new EsTools();

    /**
     * 获取默认索引类型
     * @param entity
     * @param <T>
     * @return
     */
    public <T> String getDefaultIndexName(T entity) {
        if(entity == null) {
            return null;
        }
        String className = entity.getClass().getName();
        return className.substring(className.lastIndexOf(".")+1).toLowerCase();
    }

    /**
     * 通过entity构建静态Mapping
     * @param entity
     * @param <T>
     * @return
     */
    public <T> Map<String,Map<String,String>> createDefaultMapping(T entity) {
        if(entity == null) {
            return new HashMap<>(0);
        }
        List<Field> fieldList = field2List(null, entity.getClass());
        Class<?> superClass = entity.getClass().getSuperclass();
        while (superClass != null) {
            fieldList = field2List(fieldList,superClass);
            superClass = superClass.getSuperclass();
        }
        return createMappingByFields(fieldList,entity);
    }

    /**
     * 通过field和entity构建静态mapping
     * @param fields
     * @param entity
     * @return
     */
    public Map<String,Map<String,String>> createMappingByFields(List<Field> fields,Object entity) {
        if(fields == null || entity == null) {
            return new HashMap<>(0);
        }
        Map<String, Map<String,String>> mapping = new LinkedHashMap<>();
        for(Field field : fields) {
            Map<String, Map<String,String>> newMapping = fieldHasConfigHandle(mapping, field);
            if(newMapping != null) {
                continue;
            }
            Map<String, String> configMap = this.fieldNoConfigHandle(field, entity);
            if(configMap.size() > 0) {
                mapping.put(field.getName(),configMap);
            }
        }
        return mapping;

    }

    /**
     * 将class中属性字段附加到list中
     * @param fields
     * @param claz
     * @return
     */
    private List<Field> field2List(List<Field> fields, Class<?> claz) {
        List<Field> fieldList;
        if(fields == null) {
            fieldList = new LinkedList<>();
        }else {
            fieldList = fields;
        }
        IndexIgnore ignore = claz.getDeclaredAnnotation(IndexIgnore.class);
        if(ignore != null) {
            return fields;
        }
        Field[] fieldArr = claz.getDeclaredFields();
        Arrays.stream(fieldArr).forEach(field -> {
            if(field.getDeclaredAnnotation(IndexIgnore.class) != null) {
                return;
            }
            fieldList.add(field);
        });
        fields = fieldList;
        return fields;
    }

    /**
     * 带有注解的属性处理 - 按照注解上定义的属性值配置信息
     * @param mapping
     * @param field
     * @return
     */
    private Map<String,Map<String,String>> fieldHasConfigHandle(Map<String, Map<String, String>> mapping, Field field) {
        EsFieldConfig esFieldConfig = field.getDeclaredAnnotation(EsFieldConfig.class);
        if(esFieldConfig == null) {
            return null;
        }
        String col = esFieldConfig.name();
        if("".equals(col)) {
            col = field.getName();
        }
        EsConfigEnum[] config = esFieldConfig.config();
        Map<String,String> fieldConf = new HashMap<>(2);
        for(EsConfigEnum configEnum: config) {
            fieldConf.put(configEnum.getKey(),configEnum.getValue());
        }
        mapping.put(col,fieldConf);
        return mapping;
    }

    private EsConfigEnum defaultConfig = EsConfigEnum.KEYWORD;
    /**
     * 无注解属性处理, 按照字段类型和属性赋的初始值进行配置
     * @param field
     * @param entity
     * @return
     */
    private Map<String, String> fieldNoConfigHandle(Field field, Object entity) {
        if(field == null || entity == null) {
            return new HashMap<>(0);
        }
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if(value == null) {
            return createConfigByEsConfig(defaultConfig);
        }
        Class<?> valueClass = value.getClass();
        if(valueClass == Byte.class || valueClass == byte.class) {
            return createConfigByEsConfig(EsConfigEnum.ES_BYTE);
        }
        if(valueClass == Integer.class || valueClass == int.class) {
            return createConfigByEsConfig(EsConfigEnum.ES_INT);
        }
        if(valueClass == Long.class || valueClass == long.class) {
            return createConfigByEsConfig(EsConfigEnum.ES_LONG);
        }
        if(valueClass == Double.class || valueClass == double.class) {
            return createConfigByEsConfig(EsConfigEnum.ES_DOUBLE);
        }
        if(valueClass == Boolean.class || valueClass == boolean.class) {
            return createConfigByEsConfig(EsConfigEnum.ES_BOOLEAN);
        }
        if(valueClass == Date.class) {
            return createConfigByEsConfig(EsConfigEnum.ES_DATE,EsConfigEnum.ES_DATE_FORMAT);
        }
        if(valueClass.isArray()) {
            return createConfigByEsConfig(EsConfigEnum.ES_ARRAY);
        }
        if(valueClass == String.class) {
            return textConfigHandle(String.valueOf(value));
        }
        return createConfigByEsConfig(EsConfigEnum.KEYWORD);
    }

    /**
     * String 类型处理
     * @param value
     * @return
     */
    private Map<String,String> textConfigHandle(String value) {
        if(DataCheckUtil.isDate(value)) {
            return createConfigByEsConfig(EsConfigEnum.ES_DATE,EsConfigEnum.ES_DATE_FORMAT);
        }
        if(DataCheckUtil.isJson(value)) {
            return createConfigByEsConfig(EsConfigEnum.ES_OBJECT);
        }
        if(DataCheckUtil.isIp(value)) {
            return createConfigByEsConfig(EsConfigEnum.ES_IP);
        }
        if(DataCheckUtil.containChinese(value)) {
            return createConfigByEsConfig(EsConfigEnum.TEXT,EsConfigEnum.ES_IK);
        }
        String engRegex = "^.* .*$";
        if(value.matches(engRegex)) {
            return createConfigByEsConfig(EsConfigEnum.TEXT,EsConfigEnum.ES_STANDARD);
        }
        return createConfigByEsConfig(EsConfigEnum.TEXT);
    }

    private Map<String,String> createConfigByEsConfig(EsConfigEnum ... esConfigs) {
        Map<String,String> config = new HashMap<>(1);
        Arrays.stream(esConfigs).forEach(esConfigEnum -> config.put(esConfigEnum.getKey(),esConfigEnum.getValue()));
        return config;
    }

}
