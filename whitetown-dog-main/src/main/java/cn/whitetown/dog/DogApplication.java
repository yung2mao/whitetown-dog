package cn.whitetown.dog;

import cn.whitetown.logclient.modo.WhLogConstants;
import cn.whitetown.monitor.config.MonConfConstants;
import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author GrainRain
 * @date 2020/05/30 22:26
 **/
@SpringBootApplication
@MapperScan("cn.whitetown.**.mappers")
public class DogApplication {
    private static Logger logger = WhLogConstants.sysLogger;

    public static void main(String[] args) {
        SpringApplication.run(DogApplication.class);
        logger.info(" the dog application is started");
    }
}
