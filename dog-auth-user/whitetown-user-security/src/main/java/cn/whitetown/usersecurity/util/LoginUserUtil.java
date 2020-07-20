package cn.whitetown.usersecurity.util;

import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.dto.LoginUser;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import org.springframework.cglib.beans.BeanCopier;
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

    private static BeanCopier beanCopier = BeanCopier.create(UserBasicInfo.class,LoginUser.class,false);
    /**
     * 获取登录的用户信息
     * @param user
     * @param roles
     * @return
     */
    public static LoginUser getLoginUser(UserBasicInfo user, List<UserRole> roles){
        LoginUser loginUser = new LoginUser();
        beanCopier.copy(user,loginUser,null);
        loginUser.setPassword(null);
        if(roles != null && roles.size()>0) {
            List<String> ros = new ArrayList<>();
            roles.stream().forEach(r -> ros.add(r.getName()));
            loginUser.setRoles(ros);
        }
        return loginUser;
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
