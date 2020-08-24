package cn.whitetown.smartxml.config;

import cn.whitetown.logbase.config.LogConstants;
import cn.whitetown.smartxml.manager.XmlAutoUtil;
import cn.whitetown.smartxml.manager.XmlMetaAnalyzer;
import cn.whitetown.smartxml.manager.wiml.XmlAutoUtilImpl;
import cn.whitetown.smartxml.manager.wiml.XmlMetaAnalyzerImpl;
import cn.whitetown.smartxml.meta.XmlMeta;
import cn.whitetown.smartxml.meta.XmlMetaMap;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author taixian
 * @date 2020/08/24
 **/
@Configuration
public class XmlMetaConfig {

    private Logger logger = LogConstants.SYS_LOGGER;

    private String xmlSourcePath = XmlModel.TEMPLATE_PATH;

    @Autowired
    private XmlMetaMap xmlMetaMap;

    private XmlMetaAnalyzer xmlMetaAnalyzer = new XmlMetaAnalyzerImpl();

    /**
     * 初始化xml元数据
     * @return
     */
    @Bean
    public XmlMetaMap initXmlMeta(){
        XmlMetaMap metaMap = new XmlMetaMap(new HashMap<>(4));
        File file = new File(xmlSourcePath);
        File[] files = file.listFiles();
        Arrays.stream(files).forEach(f -> {
            try {
                putMeta(f,metaMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        logger.info("the xml meta map is init,the size is >> " + metaMap.size());
        return metaMap;
    }

    /**
     * 初始化xml自动解析与封装工具
     * @return
     */
    @Bean
    public XmlAutoUtil initXmlAutoUse(){
        XmlAutoUtilImpl xmlAutoUse = new XmlAutoUtilImpl(xmlMetaMap);
        logger.info("XmlAutoUtil is init");
        return xmlAutoUse;
    }


    /**
     * 解析文件并将数据加入到xml元数据map中
     * @param file 文件
     * @param metaMap 元数据map
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void putMeta(File file, XmlMetaMap metaMap) throws IOException, ClassNotFoundException {
        if(file.isDirectory()){
            putMeta(file,metaMap);
            return;
        }
        String fileName = file.getName();
        String xmlFileSuffix = ".xml";
        if(fileName.endsWith(xmlFileSuffix)){
            String xmlName = xmlSourcePath + "/" + fileName;
            String modelName = fileName.substring(0, fileName.indexOf(xmlFileSuffix));
            String beanName = XmlModel.MODEL_BEAN.get(modelName);
            if(beanName == null) { return; }
            String xml = xmlMetaAnalyzer.readFileAsXml(xmlName);
            XmlMeta xmlMeta = xmlMetaAnalyzer.getXmlMeta(xml, Class.forName(beanName));
            metaMap.put(beanName, xmlMeta);
        }
    }
}
