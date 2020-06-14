package cn.whitetown.dog.config;

import cn.whitetown.dogbase.domain.special.WhiteExpireMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



/**
 * @author GrainRain
 * @date 2020/05/30 22:52
 **/
@Configuration
@ComponentScan("cn.whitetown")
public class DogStartConfig {
    /**
     * 初始化内存数据存储对象
     * @return
     */
    @Bean
    public WhiteExpireMap whiteExpireMap(){
        return new WhiteExpireMap();
    }
}

