package cn.whitetown.authcommon.entity.ao;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户角色分配信息
 * @author GrainRain
 * @date 2020/06/29 22:13
 **/
@Getter
@Setter
public class UserRoleConfigure {
    /**
     * 用户名
     */
    @NotBlank(message = "用户信息不能为空")
    private String username;

    /**
     * 角色列表
     */
    @NotNull(message = "角色信息不能为空")
    private Long[] roleIds;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
