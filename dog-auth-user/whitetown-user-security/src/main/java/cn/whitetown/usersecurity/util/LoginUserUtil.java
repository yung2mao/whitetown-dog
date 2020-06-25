package cn.whitetown.usersecurity.util;

import cn.whitetown.authcommon.entity.UserRole;
import cn.whitetown.authcommon.entity.vo.LoginUser;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 利用用户基本信息获取用户登录保留信息
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
        LoginUser us = new LoginUser(user.getAvatar(),user.getRealName(),user.getBirthday(),
                user.getGender(),user.getEmail(),user.getTelephone());
        if(roles != null && roles.size()>0) {
            List<String> ros = new ArrayList<>();
            roles.stream().forEach(r -> ros.add(r.getName()));
            us.setRoles(ros);
        }
        us.setUsername(user.getUsername());
        return us;
    }

    /**
     * 根据角色信息创建角色集合
     * @param roles
     * @return
     */
    public static Collection<GrantedAuthority> createRoleCollection(List<String> roles){
        Collection<GrantedAuthority> collection = new ArrayList<>();
        roles.stream().forEach(r->collection.add(new SimpleGrantedAuthority(r)));
        return collection;
    }
}
