package cn.whitetown.mshow;

import cn.whitetown.mshow.service.wiml.WebSocketServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author taixian
 * @date 2020/08/25
 **/
@SpringBootApplication
public class ShowApplication implements ApplicationRunner {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(ShowApplication.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        WebSocketServerImpl.setApplicationContext(applicationContext);
    }
}
