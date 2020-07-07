package cn.whitetown.authcommon.entity.ao;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;

/**
 * 菜单与角色配置信息
 * @author GrainRain
 * @date 2020/07/07 21:59
 **/
@Getter
@Setter
public class RoleMenuConfigure {
    /**
     * 角色ID
     */
    @NotBlank(message = "角色ID不能为空")
    private Long roleId;
    /**
     * 菜单ID项
     */
    @NotBlank(message = "菜单列表不能为空")
    private Long[] menuIds;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
