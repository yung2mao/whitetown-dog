package cn.whitetown.dogbase.config;

import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.util.SnowIDCreateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ID生成工具类初始化
 *
 * @author GrainRain
 * @date 2020/06/13
 **/
@Configuration
public class IDCreateUtilConfig {

    @Bean
    public SnowIDCreateUtil getInstance(){
        return new SnowIDCreateUtil(DogBaseConstant.CURRENT_WORK_ID);
    }
}
