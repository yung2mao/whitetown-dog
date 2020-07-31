package cn.whitetown.usersecurity.manager.impl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authea.manager.UserDetailManager;
import cn.whitetown.authea.modo.AuthUser;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.usersecurity.manager.MenuInfoManager;
import cn.whitetown.usersecurity.manager.RoleManager;
import cn.whitetown.usersecurity.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author taixian
 * @date 2020/07/30
 **/
@Service
public class UserDetailManagerImpl implements UserDetailManager {

    @Autowired
    private UserManager userManager;

    @Autowired
    private MenuInfoManager menuInfoManager;

    @Autowired
    private RoleManager roleManager;

    @Override
    public AuthUser createAuthUser(String username) {
        UserBasicInfo user = userManager.getUserByUsername(username);
        if(user != null && user.getUserStatus() == DogBaseConstant.ACTIVE_NORMAL) {
            AuthUser authUser = new AuthUser(username, user.getPassword(),user.getUserVersion());
            authUser.setUserId(user.getUserId());
            List<String> roles = this.getRolesByUserId(user.getUserId());
            List<String> authors = this.getAuthorsByUserId(user.getUserId());
            authUser.setRoles(roles);
            authUser.setAuthors(authors);
            return authUser;
        }
        return null;
    }

    @Override
    public List<String> getRoles(String username) {
        AuthUser authUser = this.createAuthUser(username);
        return authUser == null ? new ArrayList<>() : authUser.getRoles();
    }

    @Override
    public List<String> getAuthors(String username) {
        AuthUser authUser = this.createAuthUser(username);
        return authUser == null ? new ArrayList<>() : authUser.getAuthors();
    }

    @Override
    public Set<String> createAuthorsSet(AuthUser authUser) {
        if(authUser == null) {
            return new HashSet<>();
        }
        List<String> roles = authUser.getRoles();
        List<String> allAuthors = authUser.getAuthors();
        Set<String> authSet = new HashSet<>(allAuthors);
        roles.stream().forEach(role->authSet.add(role.startsWith("ROLE_") ? role : "ROLE_"+role));
        return authSet;
    }

    @Override
    public UserDetails createUserDetails() {
        return null;
    }

    protected List<String> getRolesByUserId(Long userId) {
        List<UserRole> userRoles = roleManager.queryRolesByUserId(userId);
        return userRoles.stream().filter(userRole -> userRole.getRoleStatus()==DogBaseConstant.ACTIVE_NORMAL)
                .map(userRole -> userRole.getName())
                .collect(Collectors.toList());
    }

    protected List<String> getAuthorsByUserId(Long userId) {
        List<MenuInfo> menuInfos = menuInfoManager.queryActiveMenuByUserId(userId, AuthConstant.ROOT_MENU_ID, AuthConstant.LOWEST_MENU_LEVEL);
        return menuInfos.stream().map(MenuInfo::getMenuCode).collect(Collectors.toList());
    }
}
