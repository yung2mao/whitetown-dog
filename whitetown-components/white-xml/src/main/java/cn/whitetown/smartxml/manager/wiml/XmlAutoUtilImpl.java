package cn.whitetown.smartxml.manager.wiml;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.smartxml.manager.XmlAutoUtil;
import cn.whitetown.smartxml.meta.XmlMeta;
import cn.whitetown.smartxml.meta.XmlMetaMap;
import cn.whitetown.smartxml.utils.XmlAnalyzerUtil;
import org.jsoup.nodes.Document;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 自动获取或解析xml
 *
 * @author taixian
 * @date 2020/06/15
 **/
public class XmlAutoUtilImpl implements XmlAutoUtil {
    private XmlMetaMap xmlMetaMap;

    /**
     * 元数据信息
     * @param xmlMetaMap
     */
    public XmlAutoUtilImpl(XmlMetaMap xmlMetaMap){
        if(xmlMetaMap == null) {
            throw new NullPointerException("meta map is null");
        }
        this.xmlMetaMap = xmlMetaMap;
    }

    @Override
    public <T> T readXmlAsObj(String xml,Class<T> templateClass) throws IllegalAccessException, InstantiationException {
        String beanName = templateClass.getName();
        XmlMeta xmlMeta = xmlMetaMap.get(beanName);
        if(xmlMeta==null){
            throw new NullPointerException("no meta info for template class");
        }
        String beanXmlModel = xmlMeta.getBeanXmlModel();
        if(beanXmlModel==null){
            return null;
        }
        Document document = XmlAnalyzerUtil.readXMLAsDocument(xml);

        //read attr
        List<Field> fields = this.getAllClassField(templateClass);
        //get instance
        T instance = templateClass.newInstance();
        fields.forEach(field -> {
            field.setAccessible(true);

            String fieldName = field.getName();
            XmlMeta.XmlMetaData xmlMetaData = xmlMeta.get(fieldName);
            if(xmlMetaData==null){
                return;
            }
            String value = null;
            //value in attribute
            if(!xmlMetaData.isText()){
                if(xmlMetaData.getBindAttrKey()==null){
                    value = XmlAnalyzerUtil.readAttr(document,xmlMetaData.getXPath(),xmlMetaData.getTargetAttr(),xmlMetaData.getIndex());
                }else {
                    value = XmlAnalyzerUtil.readAttrBySelectAttr(document,xmlMetaData.getXPath(),xmlMetaData.getTargetAttr(),
                            xmlMetaData.getBindAttrKey(),xmlMetaData.getBindAttrValue(),xmlMetaData.getIndex());
                }
            }else {     //value in text
                value = XmlAnalyzerUtil.readText(document,xmlMetaData.getXPath(),xmlMetaData.getIndex());
            }
            value = value == null ? null : value.trim();

            //set fields
            Class<?> type = field.getType();
            try {
                if(type == Integer.class || type == int.class){
                    field.set(instance,Integer.parseInt(value));
                }else if(type == Double.class || type == double.class){
                    field.set(instance,Double.parseDouble(value));
                }else if(type == Date.class){
                    Date date = XmlAnalyzerUtil.toDate(value);
                    field.set(instance,date);
                }else {
                    field.set(instance, value);
                }
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });
        return instance;
    }

    /**
     * 自动将传入的对象封装为xml格式文档
     * @param o
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public String getXmlByObjAndModel(Object o) throws IllegalAccessException {
        return this.getXmlByObjAndModel(xmlMetaMap.get(o.getClass().getName()).getBeanXmlModel(),o);
    }

    /**
     * 根据xml模板自动将传入的对象封装为xml格式文档
     * @param modelXml xml模板
     * @param o 需要封装的对象
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public String getXmlByObjAndModel(String modelXml,Object o) throws IllegalAccessException {
        String resultXml = modelXml;
        DataCheckUtil.checkTextNull(resultXml);
        //get all fields
        Field[] fields = o.getClass().getDeclaredFields();

        Map<String,Field> map = new LinkedHashMap<>();
        Arrays.stream(fields).forEach(field -> {
            //setAccessible
            field.setAccessible(true);
            map.put(field.getName(),field);
        });

        //super class fields
        Class<?> superclass = o.getClass().getSuperclass();
        while (superclass != null){
            Field[] superFields = superclass.getDeclaredFields();
            if(superFields != null) {
                Arrays.stream(superFields).forEach(field -> {
                    field.setAccessible(true);
                    map.put(field.getName(), field);
                });
            }
            superclass = superclass.getSuperclass();
        }
        //set fields
        for(Map.Entry<String,Field> entry:map.entrySet()){
            String key = entry.getKey();
            Field value = entry.getValue();
            Object field = value.get(o);
            if(field==null){
                field = "";
            }
            String replaceKey = "##"+key+"#@";
            resultXml = resultXml.replaceFirst(replaceKey,field.toString());
        }
        return resultXml;
    }

    /**
     * 获取模板类所有属性 - 含父类
     * @param templateClass
     * @param <T>
     * @return
     */
    private <T> List<Field> getAllClassField(Class<T> templateClass) {
        List<Field> fields = new ArrayList<>();
        Field[] childFields = templateClass.getDeclaredFields();
        Arrays.stream(childFields).forEach(field -> fields.add(field));
        //super fields
        Class<? super T> superclass = templateClass.getSuperclass();
        while (superclass != null) {
            Field[] superFields = null;
            superFields = superclass.getDeclaredFields();
            if (superFields != null) {
                Arrays.stream(superFields).forEach(field -> fields.add(field));
            }
            superclass = superclass.getSuperclass();
        }
        return fields;
    }
}
