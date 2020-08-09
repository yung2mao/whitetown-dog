package cn.whitetown.dog.destroy;

import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.dogbase.wache.WhiteExpireMap;
import cn.whitetown.logclient.modo.WhLogConstants;
import cn.whitetown.monitor.config.MonConfConstants;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 销毁时调用的方法
 * @author taixian
 * @date 2020/07/30
 **/
@Component
public class BaseDestroy implements DisposableBean {

    private Logger logger = WhLogConstants.sysLogger;

    @Autowired
    WhiteExpireMap expireMap;

    @Autowired
    MenuCacheUtil menuCacheUtil;

    @Autowired
    AuthUserCacheUtil whiteAuthUserCache;

    @Override
    public void destroy() throws Exception {
        this.cacheDataDestroy();
        logger.info("the application is closed, destroy task end");
    }

    public void cacheDataDestroy() {
        expireMap.destroy();
        menuCacheUtil.destroy();
        whiteAuthUserCache.clearAllUsersAuthors();
    }
}
