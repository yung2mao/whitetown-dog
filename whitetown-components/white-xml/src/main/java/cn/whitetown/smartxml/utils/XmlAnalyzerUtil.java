package cn.whitetown.smartxml.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author taixian
 * @date 2020/08/23
 **/
public class XmlAnalyzerUtil {
    /**
     * 通过文件路径获取Document对象
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Document readPathAsDocument(String filePath) throws IOException {
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        return document;
    }

    /**
     * 通过String类型的xml获取Document对象
     *
     * @param xml
     * @return
     */
    public static Document readXMLAsDocument(String xml) {
        return Jsoup.parse(xml);
    }

    /**
     * 通过path和属性读取第一个属性的值
     *
     * @param document
     * @param xPath
     * @param attr
     * @return
     */
    public static String readAttr(Document document, String xPath, String attr) {
        Element element = document.selectFirst(getCssSelectByPath(xPath));
        return element != null ? element.attr(attr) : null;
    }

    /**
     * 读取属性值 - 附加判断下标
     * @param document
     * @param xPath
     * @param attr
     * @param index
     * @return
     */
    public static String readAttr(Document document,String xPath,String attr,int index){
        Elements elements = document.select(getCssSelectByPath(xPath));
        if(elements == null){
            return null;
        }
        try {
            Element element = elements.get(index);
            return element != null ? element.attr(attr) : null;
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * 以list形式返回element
     * @param document
     * @param xPath
     * @return
     */
    public static List<Element> readElements(Document document, String xPath){
        Elements elements = document.select(getCssSelectByPath(xPath));
        if(elements==null){
            return null;
        }
        Iterator<Element> iterator = elements.iterator();
        List<Element> list = new ArrayList<>();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        return list.size() != 0 ? list : null;
    }

    /**
     * 读取text值 - 判断下标
     * @param document
     * @param xPath
     * @param index
     * @return
     */
    public static String readText(Document document, String xPath, int index){
        List<Element> elements = readElements(document,xPath);
        if(elements == null){
            return null;
        }
        try {
            Element element = elements.get(index);
            return element != null ? element.text() : null;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 读取所有满足path和属性的值
     *
     * @param document
     * @param xPath
     * @param attr
     * @return
     */
    public static List<String> readAttrs(Document document, String xPath, String attr) {
        Elements elements = document.select(getCssSelectByPath(xPath));
        return elements != null ? elements.eachAttr(attr) : new ArrayList<>();
    }

    /**
     * 获取一个element元素特定属性值
     *
     * @param element
     * @param attribute
     * @return
     */
    public static String readAttrByElement(Element element, String attribute) {
        return element != null ? element.attr(attribute) : null;
    }

    /**
     * 带附加属性的元素取值
     *
     * @param document
     * @param xPath
     * @param targetAttr 需要获取值的目标属性
     * @param attrKey    附加属性key
     * @param attrValue  附加属性value
     * @return
     */
    public static String readAttrBySelectAttr(Document document, String xPath, String targetAttr, String attrKey, String attrValue) {
        Elements elements = document.select(getCssSelectByPath(xPath));
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String value = element.attr(attrKey);
            if (value.equals(attrValue)) {
                return element.attr(targetAttr);
            }
        }
        return null;
    }

    /**
     * 读取绑定了附加属性的数据 - 判断下标形式
     * @param document
     * @param xPath
     * @param targetAttr
     * @param attrKey
     * @param attrValue
     * @param index
     * @return
     */
    public static  String readAttrBySelectAttr(Document document, String xPath,
                                               String targetAttr, String attrKey, String attrValue,int index) {
        List<Element> elements = readElementBySelectAttr(document,xPath,attrKey,attrValue);
        try {
            Element element = elements.get(index);
            return element != null ? element.attr(targetAttr) : null;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 以迭代器形式返回满足带附加属性的所有element
     * @param document
     * @param xPath
     * @param attrKey
     * @param attrValue
     * @return
     */
    public static List<Element> readElementBySelectAttr(Document document, String xPath, String attrKey, String attrValue) {
        Elements eles = document.select(getCssSelectByPath(xPath));
        if(eles==null){
            return null;
        }

        Iterator<Element> iterator = eles.iterator();
        List<Element> list = new ArrayList<>();
        while (iterator.hasNext()){
            Element element = iterator.next();
            String value = element.attr(attrKey).trim();
            if (value.equals(attrValue)) {
                list.add(element);
            }
        }
        return  list.size() != 0 ? list : null;
    }

    /**
     * xpath路径解析
     *
     * @param xPath
     * @return
     */
    public static String getCssSelectByPath(String xPath) {
        if (xPath == null || "".equals(xPath)) {
            throw new NullPointerException();
        }
        StringBuilder builder = new StringBuilder("");
        String[] paths = xPath.split("/");
        for (String path : paths) {
            builder.append(path).append(" ");
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 1);
    }

    /**
     * 以Integer形式获取path下属性值
     *
     * @param document
     * @param xPath
     * @param attr
     * @return
     */
    public static Integer readAttrAsInt(Document document, String xPath, String attr) {
        String str = readAttr(document, xPath, attr);
        return format(str);
    }

    public static Integer readAttrBySelectAttrAsInt(Document document, String xPath, String targetAttr, String attrKey, String attrValue) {
        String str = readAttrBySelectAttr(document, xPath, targetAttr, attrKey, attrValue);
        return format(str);
    }

    private static Integer format(String str) {
        if (str != null) {
            try {
                return Integer.parseInt(str);
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * 读取document下的节点的内容
     *
     * @param document
     * @param xPath
     * @return
     */
    public static String readText(Document document, String xPath) {
        Element element = document.selectFirst(getCssSelectByPath(xPath));
        return element.text();
    }


    /**
     * 读取时间格式的值
     *
     * @param document
     * @param xPath
     * @param attr
     * @return
     */
    public static Date readDateAttr(Document document, String xPath, String attr) {
        Element element = document.selectFirst(getCssSelectByPath(xPath));
        return toDate(element.attr(attr));
    }

    /**
     * 时间转换
     *
     * @param str
     * @return
     */
    public static Date toDate(String str) {
        String pattern = "";
        if (str.length() == 14) {
            pattern = "yyyyMMddHHmmss";
        } else if (str.length() == 12) {
            pattern = "yyyyMMddHHmm";
        } else if (str.length() == 10) {
            pattern = "yyyyMMddHH";
        } else if (str.length() == 8) {
            pattern = "yyyyMMdd";
        } else if (str.length() == 6) {
            pattern = "yyyyMM";
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat(pattern, Locale.US);
            Date date = sf.parse(str);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从element下读取时间格式的属性值
     *
     * @param element
     * @param path
     * @param attr
     * @return
     */
    public static Date readDateByElement(Element element, String path, String attr) {
        String date = element.selectFirst(path).attr(attr);
        return toDate(date);
    }

    /**
     * 根据目标属性来获取下一个兄弟节点的属性值
     *
     * @param document
     * @param xPath
     * @param targetAttr
     * @param attrKey
     * @param attrValue
     * @return
     */
    public static String readNextAttrBySelectAttr(Document document, String xPath, String targetAttr, String attrKey, String attrValue, String targetElement) {
        Elements elements = document.select(getCssSelectByPath(xPath));
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String value = element.attr(attrKey);
            if (value.equals(attrValue)) {
                Elements sibls = element.siblingElements();
                Iterator<Element> iterator1 = sibls.iterator();
                while (iterator1.hasNext()) {
                    Element target = iterator1.next();
                    if (target.tagName().equalsIgnoreCase(targetElement)) {
                        return target.attr(targetAttr);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据目标属性来获取兄弟节点的内容
     *
     * @param document
     * @param xPath
     * @param attrKey
     * @param attrValue
     * @return
     */
    public static String readNextTextBySelectAttr(Document document, String xPath, String attrKey, String attrValue, String targetElement) {
        Elements elements = document.select(getCssSelectByPath(xPath));
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String value = element.attr(attrKey);
            if (value.equals(attrValue)) {
                Elements slibs = element.siblingElements();
                Iterator<Element> iterator1 = slibs.iterator();
                while (iterator1.hasNext()) {
                    Element target = iterator1.next();
                    if (target.tagName().equalsIgnoreCase(targetElement)) {
                        return target.text();
                    }
                }
            }
        }
        return null;
    }


    /**
     * 根绝多个属性获取element
     *
     * @param document
     * @param xPath
     * @param attrKey
     * @param attrValue
     * @return
     */
    public static Element getElementByAttrs(Document document, String xPath, String[] attrKey, String attrValue[]) {
        Elements elements = document.select(getCssSelectByPath(xPath));
        Iterator<Element> iterator = elements.iterator();
        Boolean flag = true;
        while (iterator.hasNext()) {
            flag = true;
            Element element = iterator.next();
            for (int i = 0; i < attrKey.length; i++) {
                if (!element.attr(attrKey[i]).equalsIgnoreCase(attrValue[i])) {
                    flag = false;
                }
            }
            if (flag) {
                return element;
            }
        }
        return null;
    }

    /**
     * 根绝特定属性的值获取Element兄弟Element的路径下获取某一节点的attr
     *
     * @param element
     * @param xPath
     * @param targetAttr
     * @param attrKey
     * @param attrValue
     * @param targetElement
     * @return
     */
    public static String readAttrBySiblElement(Element element, String xPath, String targetAttr, String attrKey, String attrValue, String targetElement, String targetSibl) {
        Elements sibls = element.siblingElements();
        Iterator<Element> iterator1 = sibls.iterator();
        while (iterator1.hasNext()) {
            Element sibl = iterator1.next();
            if (sibl.tagName().equalsIgnoreCase(targetElement)) {
                Elements targets = sibl.select(getCssSelectByPath(xPath));
                Iterator<Element> iterator = targets.iterator();
                while (iterator.hasNext()) {
                    Element target = iterator.next();
                    if (attrKey == null && attrValue == null){
                        return target.attr(targetAttr);
                    }else if (target.attr(attrKey).equalsIgnoreCase(attrValue)) {
                        Elements es = target.siblingElements();
                        Iterator<Element> iterator2 = es.iterator();
                        while (iterator2.hasNext()) {
                            Element end = iterator2.next();
                            if (end.tagName().equalsIgnoreCase(targetSibl)) {
                                return end.attr(targetAttr);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根绝特定属性的值获取Element兄弟Element的路径下获取某一节点的内容
     *
     * @param element
     * @param xPath
     * @param attrKey
     * @param attrValue
     * @param targetElement
     * @return
     */
    public static String readTextByElement(Element element, String xPath, String attrKey, String attrValue, String targetElement, String targetSibl) {
        Elements sibls = element.siblingElements();
        Iterator<Element> iterator1 = sibls.iterator();
        while (iterator1.hasNext()) {
            Element sibl = iterator1.next();
            if (sibl.tagName().equalsIgnoreCase(targetElement)) {
                Elements targets = sibl.select(getCssSelectByPath(xPath));
                Iterator<Element> iterator = targets.iterator();
                while (iterator.hasNext()) {
                    Element target = iterator.next();
                    if (attrKey == null && attrValue == null){
                        return target.text();
                    }else if (target.attr(attrKey).equalsIgnoreCase(attrValue)) {
                        Elements es = target.siblingElements();
                        Iterator<Element> iterator2 = es.iterator();
                        while (iterator2.hasNext()) {
                            Element end = iterator2.next();
                            if (end.tagName().equalsIgnoreCase(targetSibl)) {
                                return end.text();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根绝特定属性的值获取Element兄弟Element某一节点的attr
     *
     * @param element
     * @param targetAttr
     * @param targetElement
     * @return
     */
    public static String readAttrBySiblElement(Element element, String targetAttr,String targetElement) {
        Elements sibls = element.siblingElements();
        Iterator<Element> iterator1 = sibls.iterator();
        while (iterator1.hasNext()) {
            Element target = iterator1.next();
            if (target.tagName().equalsIgnoreCase(targetElement)) {
                return target.attr(targetAttr);
            }
        }
        return null;
    }

    /**
     * 获取Element兄弟Element某一节点的内容
     *
     * @param element
     * @param targetElement
     * @return
     */
    public static String readTextBySiblElement(Element element,String targetElement) {
        Elements slibs = element.siblingElements();
        Iterator<Element> iterator1 = slibs.iterator();
        while (iterator1.hasNext()) {
            Element target = iterator1.next();
            if (target.tagName().equalsIgnoreCase(targetElement)) {
                return target.text();
            }
        }
        return null;
    }
}
