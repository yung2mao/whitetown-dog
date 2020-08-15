package cn.whitetown.esconfig.config;

import cn.whitetown.esconfig.modo.EsIndicesMap;
import cn.whitetown.logbase.config.LogConstants;
import org.apache.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * es初始化配置类
 * @author taixian
 * @date 2020/08/14
 **/
@Configuration
public class EsInitConfig {

    private Logger logger = LogConstants.DB_LOGGER;

    @Autowired
    private RestHighLevelClient esClient;

    @Bean
    public EsIndicesMap esIndicesMap() {
        return new EsIndicesMap();
    }

}
