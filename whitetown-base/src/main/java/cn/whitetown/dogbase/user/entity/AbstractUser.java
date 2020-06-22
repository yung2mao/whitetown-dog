package cn.whitetown.dogbase.user.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author GrainRain
 * @date 2020/05/25 23:06
 **/
@Setter
@Getter
public abstract class AbstractUser {
    /**
     * 用户id
     */
    @TableId
    protected Long userId;
    /**
     * 用户名 - 每个用户唯一
     */
    @NotBlank
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
