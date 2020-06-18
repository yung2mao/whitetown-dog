package cn.whitetown.dogbase.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author GrainRain
 * @date 2020/05/25 23:06
 **/
public abstract class AbstractUser {
    /**
     * 用户id
     */
    @TableId
    protected Long userId;
    /**
     * 用户名 - 每个用户唯一
     */
    @NotNull
    @NotEmpty
    protected String username;
    /**
     * 用户密码
     */
    protected String password;

    /**
     * 用户角色列表
     */
    @TableField(exist = false)
    protected List<String> roles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
