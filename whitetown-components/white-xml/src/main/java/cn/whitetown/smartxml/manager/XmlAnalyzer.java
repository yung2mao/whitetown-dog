package cn.whitetown.smartxml.manager;

import cn.whitetown.smartxml.meta.XmlMeta;

import java.io.IOException;

/**
 *
 * XML初始化解析
 *
 * @author taixian
 * @date 2020/05/18
 **/
public interface XmlAnalyzer {
    /**
     * 将xml模板解析为xml元数据
     * @param beanXml 模板文件
     * @param claz 模板类
     * @param <T>
     * @return
     */
    <T> XmlMeta getXmlMeta(String beanXml, Class<T> claz);

    /**
     * 读取指定路径下的文件为xml
     * @param fileName 文件名称
     * @return
     * @throws IOException
     */
    String readFileAsXml(String fileName) throws IOException;
}
