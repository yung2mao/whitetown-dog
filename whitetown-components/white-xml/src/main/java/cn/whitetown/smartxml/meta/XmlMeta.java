package cn.whitetown.smartxml.meta;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * 解析xml获取的元数据信息
 *
 * @author taixian
 * @date 2020/05/15
 **/
public class XmlMeta extends HashMap<String, XmlMeta.XmlMetaData> {
    private String beanName;
    private String beanXmlModel;

    public XmlMeta(String beanName, String beanXmlModel) {
        this.beanName = beanName;
        this.beanXmlModel = beanXmlModel;
    }

    public XmlMeta() {
    }

    public XmlMeta(int initialCapacity, float loadFactor, String beanName) {
        super(initialCapacity, loadFactor);
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanXmlModel() {
        return beanXmlModel;
    }

    public void setBeanXmlModel(String beanXmlModel) {
        this.beanXmlModel = beanXmlModel;
    }

    @Override
    public XmlMetaData put(String beanField, XmlMetaData value) {
        return super.put(beanField, value);
    }

    public XmlMetaData put(String beanField,
                           String xPath, String targetAttr, String bindAttrKey, String bindAttrValue){
        return super.put(beanField,new XmlMetaData(xPath,targetAttr,bindAttrKey,bindAttrValue));
    }

    public XmlMetaData put(String beanField,String xPath, String targetAttr, String bindAttrKey,
                           String bindAttrValue, String textAttr,
                           boolean isText, int index){
        return super.put(beanField,new XmlMetaData(xPath,targetAttr,bindAttrKey,bindAttrValue,textAttr,isText,index));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /*static class*/
    static class XmlMetaData {
        private String xPath;
        private String targetAttr;
        private String bindAttrKey;
        private String bindAttrValue;
        private String textAttr;
        private boolean isText;
        private int index;

        public XmlMetaData() {
        }

        public XmlMetaData(String xPath, String targetAttr, String bindAttrKey, String bindAttrValue) {
            this.xPath = xPath;
            this.targetAttr = targetAttr;
            this.bindAttrKey = bindAttrKey;
            this.bindAttrValue = bindAttrValue;
        }

        public XmlMetaData(String xPath, String targetAttr, String bindAttrKey,
                           String bindAttrValue, String textAttr,
                           boolean isText, Integer index) {
            this.xPath = xPath;
            this.targetAttr = targetAttr;
            this.bindAttrKey = bindAttrKey;
            this.bindAttrValue = bindAttrValue;
            this.textAttr = textAttr;
            this.isText = isText;
            this.index = index;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }
}
