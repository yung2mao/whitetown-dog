package cn.whitetown.authea.modo;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限校验所需基本User类
 * @author taixian
 * @date 2020/07/30
 **/
@Getter
public class AuthUser {
    protected Long userId;
    protected String username;
    protected String password;
    protected Integer userVersion;
    protected List<String> roles = new ArrayList<>();
    protected List<String> authors = new ArrayList<>();

    public AuthUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthUser(String username, String password, Integer userVersion) {
        this.username = username;
        this.password = password;
        this.userVersion = userVersion;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
}
