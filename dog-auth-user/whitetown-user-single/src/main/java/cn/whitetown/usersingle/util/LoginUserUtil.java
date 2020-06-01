package cn.whitetown.usersingle.util;

import cn.whitetown.dogbase.user.entity.LoginUser;
import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import cn.whitetown.dogbase.user.entity.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrainRain
 * @date 2020/05/30 16:46
 **/
public class LoginUserUtil {
    /**
     * 获取登录的用户信息
     * @param user
     * @param roles
     * @return
     */
    public static LoginUser getLoginUser(UserBasicInfo user, List<UserRole> roles){
        List<Long> roIds = new ArrayList<>();
        List<String> ros = new ArrayList<>();
        roles.stream().forEach(r-> {
            roIds.add(r.getRoleId());
            ros.add(r.getName());
        });
        LoginUser us = new LoginUser();
        us.setUserId(user.getUserId());
        us.setUsername(user.getUsername());
        us.setPassword(user.getPassword());
        us.setRoleIds(roIds);
        us.setRoles(ros);
        return us;
    }
}
