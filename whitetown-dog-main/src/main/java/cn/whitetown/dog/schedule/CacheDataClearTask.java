package cn.whitetown.dog.schedule;

import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 内存存储数据定期清理任务
 * @author taixian
 * @date 2020/07/31
 **/
@EnableScheduling
@Configuration
public class CacheDataClearTask {

    @Autowired
    private MenuCacheUtil menuCacheUtil;

    @Autowired
    private AuthUserCacheUtil authUserCacheUtil;

    /**
     * 定时扫描过期数据
     * @return
     */
    @Scheduled(initialDelay = 2000,fixedDelay = 3600000)
    public void run(){
        menuCacheUtil.reset();
        authUserCacheUtil.clearAllUsersAuthors();
    }
}
