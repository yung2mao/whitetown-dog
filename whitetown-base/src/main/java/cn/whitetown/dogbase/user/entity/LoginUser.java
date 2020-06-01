package cn.whitetown.dogbase.user.entity;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * 登录用户基础信息
 * @author GrainRain
 * @date 2020/05/30 15:21
 **/
public class LoginUser extends AbstractUser{
    private List<Long> roleIds;
    private List<String> roles;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
