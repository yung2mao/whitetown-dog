package cn.whitetown.dog.config;

import cn.whitetown.dogbase.common.memdata.MultiWhiteExpireMap;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.DefaultBeanTransFactory;
import cn.whitetown.dogbase.db.factory.DefaultQueryConditionFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author GrainRain
 * @date 2020/05/30 22:52
 **/
@Configuration
@ComponentScan("cn.whitetown")
public class DogStartConfig {
    /**
     * 初始化缓存容器
     * @return
     */
    @Bean
    public WhiteExpireMap whiteExpireMap(){
        return new MultiWhiteExpireMap(4);
    }

    /**
     * 初始化数据库查询条件工厂
     * @return
     */
    @Bean
    public QueryConditionFactory queryConditionFactory(){
        return new DefaultQueryConditionFactory();
    }

    /**
     * 初始化bean转换工厂
     * @return
     */
    @Bean
    public BeanTransFactory beanTransFactory(){
        return new DefaultBeanTransFactory();
    }

}

