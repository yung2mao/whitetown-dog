package cn.whitetown.esconfig.config;

import cn.whitetown.esconfig.manager.EsDocManager;
import cn.whitetown.esconfig.manager.EsIndicesManager;
import cn.whitetown.esconfig.manager.EsSearchManager;
import cn.whitetown.esconfig.manager.wiml.DefaultDocManager;
import cn.whitetown.esconfig.manager.wiml.DefaultIndicesManager;
import cn.whitetown.esconfig.manager.wiml.DefaultSearchManager;
import cn.whitetown.esconfig.modo.EsIndicesMap;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * es初始化配置类
 * @author taixian
 * @date 2020/08/14
 **/
@Configuration
public class EsInitConfig {

    /**
     * 初始化索引存储结构
     * @return
     */
    @Bean
    @Lazy
    public EsIndicesMap esIndicesMap() {
        return new EsIndicesMap();
    }

    /**
     * 初始化索引操作bean
     * @return
     */
    @Bean
    @Lazy
    public EsIndicesManager indicesManager() {
        return new DefaultIndicesManager();
    }

    /**
     * 初始化文档操作bean
     * @return
     */
    @Bean
    @Lazy
    public EsDocManager esDocManager() {
        return new DefaultDocManager();
    }

    @Bean
    @Lazy
    public EsSearchManager searchManager() {
        return new DefaultSearchManager();
    }
}
