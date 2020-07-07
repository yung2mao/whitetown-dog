package cn.whitetown.authcommon.util;

import cn.whitetown.authcommon.entity.vo.MenuTree;

/**
 * 菜单信息缓存操作
 * @author GrainRain
 * @date 2020/07/07 22:05
 **/
public interface MenuCacheUtil {

    /**
     * 初始化菜单操作
     */
    void init();

    /**
     * 销毁时操作
     */
    void destroy();
}
