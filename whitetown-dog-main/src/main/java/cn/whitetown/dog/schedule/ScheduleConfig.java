package cn.whitetown.dog.schedule;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.nio.charset.StandardCharsets;

/**
 * 定时任务执行配置
 * @author GrainRain
 * @date 2020/05/28 22:18
 **/
@EnableScheduling
@Configuration
public class ScheduleConfig {

    /**
     * quartz方式，配置Scheduler实例
     *
     * @author fengshuonan
     * @Date 2019/2/24 19:03
     */
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        return new SchedulerFactoryBean();
    }


}
