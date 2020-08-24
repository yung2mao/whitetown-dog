package cn.whitetown.smartxml.config;

import cn.whitetown.smartxml.manager.XmlMetaAnalyzer;
import cn.whitetown.smartxml.manager.wiml.XmlMetaAnalyzerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author taixian
 * @date 2020/08/24
 **/
@Configuration
public class XmlInitConfig {
    /**
     * 初始化xml元数据解析类
     * @return
     */
    @Bean
    public XmlMetaAnalyzer initXmlAnalyzer() {
        return new XmlMetaAnalyzerImpl();
    }
}
