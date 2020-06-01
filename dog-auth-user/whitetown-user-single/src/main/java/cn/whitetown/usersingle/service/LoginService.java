package cn.whitetown.usersingle.service;

import cn.whitetown.dogbase.user.entity.UserBasicInfo;

/**
 * @author GrainRain
 * @date 2020/05/29 22:21
 **/
public interface LoginService {
    String checkUsernameAndPassword(String username, String password);

    String checkLogin(String token);

    UserBasicInfo getUserInfo(String token);

}
