package cn.whitetown.smartxml.meta;

import java.util.Map;

/**
 * 元数据存储map
 * @author taixian
 * @date 2020/08/24
 **/
public class XmlMetaMap {
    private Map<String, XmlMeta> xmlMetaMap;

    public XmlMetaMap(Map<String, XmlMeta> xmlMetaMap) {
        this.xmlMetaMap = xmlMetaMap;
    }

    public XmlMeta put(String key,XmlMeta value) {
        return xmlMetaMap.put(key,value);
    }

    public XmlMeta get(String key) {
        return xmlMetaMap.get(key);
    }

    public XmlMeta remove(String key) {
        return xmlMetaMap.remove(key);
    }

    public boolean containsKey(String key) {
        return xmlMetaMap.containsKey(key);
    }

    public int size() {
        return xmlMetaMap.size();
    }

    public void destroy() {
        xmlMetaMap.clear();
    }

}
