package cn.whitetown.dog.destroy;

import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
import cn.whitetown.usersecurity.util.AuthUserCacheUtil;
import cn.whitetown.usersecurity.util.define.WhiteAuthUserCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private Log logger = LogFactory.getLog(BaseDestroy.class);

    @Autowired
    WhiteExpireMap expireMap;

    @Autowired
    MenuCacheUtil menuCacheUtil;

    @Autowired
    AuthUserCacheUtil whiteAuthUserCache;

    @Override
    public void destroy() throws Exception {
        expireMap.destroy();
        menuCacheUtil.destroy();
        whiteAuthUserCache.clearAllUsersAuthors();
        logger.warn("the application is closed, destroy task end");
    }
}
