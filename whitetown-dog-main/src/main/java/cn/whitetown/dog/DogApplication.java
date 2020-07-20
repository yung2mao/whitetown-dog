package cn.whitetown.dog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author GrainRain
 * @date 2020/05/30 22:26
 **/
@SpringBootApplication
@MapperScan("cn.whitetown.**.mappers")
public class DogApplication {
    private static Log logger = LogFactory.getLog(DogApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DogApplication.class);
        logger.warn(" >> the dog application is started");
    }
}
