package cn.whitetown.dog.destroy;

import cn.whitetown.authcommon.util.MenuCacheUtil;
import cn.whitetown.dogbase.common.memdata.WhiteExpireMap;
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

    @Autowired
    WhiteExpireMap expireMap;

    @Autowired
    MenuCacheUtil menuCacheUtil;

    @Override
    public void destroy() throws Exception {
        expireMap.destroy();
        menuCacheUtil.destroy();
    }
}
