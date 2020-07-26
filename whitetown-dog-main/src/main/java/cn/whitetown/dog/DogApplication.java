package cn.whitetown.dog;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;


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

/*    @Override
    public void run(ApplicationArguments args) throws Exception {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        for(Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            String path = patterns.toArray(new String[0])[0];
            Annotation[] annotations = entry.getValue().getMethod().getAnnotations();
            for(Annotation annotation:annotations) {
                if(annotation.annotationType().getAnnotationsByType(RequestMapping.class) != null) {
                    System.out.println(path + "," +annotation);
                }
            }
        }
    }*/
}
