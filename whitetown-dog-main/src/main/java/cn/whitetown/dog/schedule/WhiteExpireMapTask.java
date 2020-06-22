package cn.whitetown.dog.schedule;

import cn.whitetown.dogbase.common.memdata.SingleWhiteExpireMap;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMapClean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 过期数据扫描实现
 * @author GrainRain
 * @date 2020/05/28 22:56
 **/
@EnableScheduling
@Configuration
public class WhiteExpireMapTask implements WhiteExpireMapClean {

    @Autowired
    private WhiteExpireMap whiteExpireMap;

    public WhiteExpireMapTask(){}

    @Override
    public <K, V> void scanAndClean(WhiteExpireMap<K, V> whiteExpireMap) {
        whiteExpireMap.expireScan();
    }

    /**
     * 定时扫描过期数据
     * @return
     */
    @Scheduled(initialDelay = 2000,fixedDelay = 60000)
    public void run(){
        this.scanAndClean(whiteExpireMap);
    }
}
