package cn.whitetown.smartxml.manager.wiml;

import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.smartxml.manager.XmlMetaAnalyzer;
import cn.whitetown.smartxml.meta.XmlMeta;
import cn.whitetown.smartxml.utils.XmlAnalyzerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * xml元数据解析
 *
 * @author taixian
 * @date 2020/06/15
 **/
public class XmlMetaAnalyzerImpl implements XmlMetaAnalyzer {

    /**
     * 根据xml和对象类型自动解析xml并返回元数据信息
     * @param beanXml
     * @param templateClass
     * @param <T>
     * @return
     */
    @Override
    public <T> XmlMeta getXmlMeta(String beanXml, Class<T> templateClass){
        DataCheckUtil.checkTextNull(beanXml);
        XmlMeta xmlMeta = new XmlMeta();
        xmlMeta.setBeanName(templateClass.getName());
        xmlMeta.setBeanXmlModel(beanXml);
        //模板类所有属性名称信息 - 包含父类
        List<String> fieldNames = this.getAllClassFieldNames(templateClass);
        Document document = Jsoup.parse(beanXml);
        /*
         * map结构: key:attrValue或text,
         *         value : Map.Entry( key : Attribute或null（null说明参数在text中）,
         *                            value : Element)
         * 通过这一结构可根据key快速定位Element
         */
        Map<String, Map.Entry<Attribute, Element>> attrMap = this.xmlValWithEle(document);
        //模板类属性 - 匹配xml中位置
        fieldNames.forEach(fieldName->{
            String regex = "##"+fieldName+"#@";
            Map.Entry<Attribute, Element> elementEntry = attrMap.get(regex);
            //element exists
            if(elementEntry == null) {
                return;
            }
            XmlMeta.XmlMetaData xmlMetaData = null;
            //element in attribute value
            if(elementEntry.getKey() != null) {
                xmlMetaData = this.getMetaFromAttrEntry(elementEntry, document, fieldName);
            }else { //element in text
                Element element = elementEntry.getValue();
                String xPath = this.createXPathByEle(element);
                xmlMetaData = new XmlMeta.XmlMetaData();
                xmlMetaData.setXPath(xPath);
                xmlMetaData.setText(true);
                //反向判断
                List<Element> elements = XmlAnalyzerUtil.readElements(document, xPath);
                checkPath(fieldName,elements,xmlMetaData);
            }
            xmlMeta.put(fieldName, xmlMetaData);
        });
        return xmlMeta;
    }

    /**
     * 读取文件为字符串
     * @param fileName
     * @return
     * @throws IOException
     */
    @Override
    public String readFileAsXml(String fileName) throws IOException {
        File file = new File(fileName);
        InputStream in = new FileInputStream(file);
        byte[] bytes = new byte[1024*1024];
        int len = -1;
        StringBuffer buffer = new StringBuffer("");
        while ((len=in.read(bytes))!= -1){
            buffer.append(new String(bytes,0,len));
        }
        return buffer.toString();
    }

    /**
     * 数据审查 - 明确数据所在可能的index
     * @param fieldName
     * @param elements
     * @param xmlMetaData
     */
    void checkPath(String fieldName,List<Element> elements, XmlMeta.XmlMetaData xmlMetaData){
        if(elements==null){
            return;
        }

        if(elements.size()==1){
            xmlMetaData.setIndex(0);
        }else {
            for (int i = 0; i < elements.size(); i++) {
                if(elements.get(i).toString().contains("##"+fieldName+"#@")){
                    xmlMetaData.setIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * 根据element获取xpath
     * @param element
     * @return
     */
    public String createXPathByEle(Element element){
        if(element==null){
            return null;
        }
        String tagName = element.tag().getName();
        Stack<String> tagStack = new Stack<>();
        Element e = element.parent();
        while (e != null) {
            tagStack.push(e.tagName());
            e = e.parent();
            if (e.tagName().equals("body")) {
                break;
            }
        }

        StringBuffer buffer = new StringBuffer("");
        while (tagStack.size() != 0) {
            buffer.append("/").append(tagStack.pop());
        }
        //xPath
        String xPath = buffer.append("/").append(tagName).toString();
        return xPath;
    }

    /**
     * xml document基本解析,拆分属性/text值与element对应关系
     * @param document
     * @return
     */
    private Map<String,Map.Entry<Attribute,Element>> xmlValWithEle(Document document) {
        Elements allElements = document.getAllElements();
        Map<String,Map.Entry<Attribute,Element>> attrMap = new HashMap<>(4);

        allElements.stream().forEach(element -> {
            List<Attribute> attributes = element.attributes().asList();
            //首先处理所有的attribute
            attributes.forEach(attribute -> {
                String key = attribute.getValue();
                attrMap.put(key, new AbstractMap.SimpleEntry<>(attribute,element));
            });
            //如果值在text中，取出text值放入map中待做判断
            if(element.childNodeSize() == 1){
                String text = element.text();
                if(!DataCheckUtil.checkTextNullBool(text)){
                    attrMap.put(text,new AbstractMap.SimpleEntry<>(null,element));
                }
            }
        });
        return attrMap;
    }

    /**
     * 解析filedName对应的唯一元数据信息
     * @param elementEntry
     * @param document
     * @param fieldName
     * @return
     */
    private XmlMeta.XmlMetaData getMetaFromAttrEntry(Map.Entry<Attribute, Element> elementEntry, Document document, String fieldName) {
        //targetAttr
        String targetAttr = elementEntry.getKey().getKey();
        Element element = elementEntry.getValue();
        String xPath = this.createXPathByEle(element);

        List<Attribute> attributes = new ArrayList<>();
        elementEntry.getValue().attributes().asList().stream().forEach(attribute -> attributes.add(attribute));
        attributes.remove(elementEntry.getKey());
        for (int i = 0; i < attributes.size(); i++) {
            if(attributes.get(i).getValue().matches("^##.*#@$")){
                attributes.remove(i);
            }
        }
        Attribute at = null;
        if (attributes.size() > 0) {
            at = attributes.get(0);
        }
        String bindAttrKey = null;
        String bindAttrValue = null;
        if (at != null) {
            //bindAttrKey and bindAttrValue
            bindAttrKey = at.getKey();
            bindAttrValue = at.getValue().trim();
        }
        XmlMeta.XmlMetaData xmlMetaData = new XmlMeta.XmlMetaData();
        xmlMetaData.setXPath(xPath);
        xmlMetaData.setTargetAttr(targetAttr);
        xmlMetaData.setBindAttrKey(bindAttrKey);
        xmlMetaData.setBindAttrValue(bindAttrValue);
        xmlMetaData.setText(false);
        //反向判断
        if(bindAttrKey ==null){
            List<Element> elements = XmlAnalyzerUtil.readElements(document, xPath);
            checkPath(fieldName,elements,xmlMetaData);
        }else {
            List<Element> elements = XmlAnalyzerUtil.readElementBySelectAttr(document, xPath, bindAttrKey, bindAttrValue);
            checkPath(fieldName,elements,xmlMetaData);
        }
        return xmlMetaData;
    }

    /**
     * 获取class和super class中的属性字段名
     * @param claz
     * @param <T>
     * @return
     */
    private <T> List<String> getAllClassFieldNames(Class<T> claz) {

        Field[] fields = claz.getDeclaredFields();

        //field list
        List<String> fieldNames = new ArrayList<>();
        Arrays.stream(fields).forEach(field -> fieldNames.add(field.getName()));
        //super class
        Class<? super T> superclass = claz.getSuperclass();
        while (superclass != null) {
            Field[] superFields = null;
            superFields = superclass.getDeclaredFields();
            if (superFields != null) {
                Arrays.stream(superFields).forEach(field -> fieldNames.add(field.getName()));
            }
            superclass = superclass.getSuperclass();
        }
        return fieldNames;
    }
}
