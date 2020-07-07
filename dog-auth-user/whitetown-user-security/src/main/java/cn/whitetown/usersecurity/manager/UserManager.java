package cn.whitetown.usersecurity.manager;

import cn.whitetown.authcommon.entity.po.UserBasicInfo;

/**
 * 用户管理通用接口
 * @author GrainRain
 * @date 2020/07/07 21:56
 **/
public interface UserManager {

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    UserBasicInfo getUserByUsername(String username);
}
