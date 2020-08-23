package cn.whitetown.smartxml.manager;

/**
 * XML自动解析与封装
 *
 * @author taixian
 * @date 2020/05/18
 **/
public interface XmlAutoUse {
    /**
     * 自动读取xml为对象
     * @param xml 传入的xml
     * @param claz 对象类型
     * @param <T> 返回对象类别
     * @return
     * @throws Exception
     */
    <T> T readXmlAsObj(String xml, Class<T> claz) throws Exception;

    /**
     * 自动将传入的对象封装为xml格式文档
     * @param o
     * @return
     * @throws IllegalAccessException
     */
    String getXmlByObjAndModel(Object o) throws IllegalAccessException;

    /**
     * 根据xml模板自动将传入的对象封装为xml格式文档
     * @param modelXml xml模板
     * @param o 需要封装的对象
     * @return
     * @throws IllegalAccessException
     */
    String getXmlByObjAndModel(String modelXml, Object o) throws IllegalAccessException;
}
