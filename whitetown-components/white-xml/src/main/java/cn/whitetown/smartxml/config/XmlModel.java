package cn.whitetown.smartxml.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * xml模板与类对应关系配置
 * @author taixian
 * @date 2020/08/24
 **/
class XmlModel {
    static final Properties XML_BASE_CONFIG;
    static final Map<String,String> MODEL_BEAN = new HashMap<>();

    static final String TEMPLATE_PATH;

    static {
        XML_BASE_CONFIG = initBaseConfig("xml-config.properties");
        Properties modelConfig = initBaseConfig("xml-template.properties");
        assert modelConfig != null;
        modelConfig.forEach((k, v)->{
                    MODEL_BEAN.put(k.toString(),v.toString());
        });

        TEMPLATE_PATH = XML_BASE_CONFIG.getProperty("template.path");
    }

    /**
     * 获取配置信息
     * @param configName
     * @return
     */
    public static Properties initBaseConfig(String configName) {
        InputStream in = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(configName);
        Properties pro = new Properties();
        try {
            pro.load(in);
            return pro;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
