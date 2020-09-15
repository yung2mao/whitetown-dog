package cn.whitetown.authea.service.impl;

import cn.whitetown.authea.manager.UserDetailManager;
import cn.whitetown.authea.service.WhiteUserDetailService;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

/**
 * UserDetailService的demo实现
 * 实际开发时需根据自己的需求提供相应的实现
 * @author taixian
 * @date 2020/07/27
 **/
public class WhiteUserDetailDemo implements WhiteUserDetailService {
    private UserDetailManager userDetailManager;

    public WhiteUserDetailDemo(UserDetailManager userDetailManager) {
        this.userDetailManager = userDetailManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailManager.createUserDetails();
    }

    @Override
    public UserDetails loadBasicUserDetails(String username) {
        String password = "123456";
        return new User(username, WhiteToolUtil.md5Hex(password),new ArrayList<>());
    }
}
