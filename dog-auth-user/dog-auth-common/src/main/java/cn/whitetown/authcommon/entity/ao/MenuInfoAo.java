package cn.whitetown.authcommon.entity.ao;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 菜单前端传递参数信息
 * @author GrainRain
 * @date 2020/07/06 22:01
 **/
@Getter
@Setter
public class MenuInfoAo {
    /**
     * 菜单ID
     */
    @Min(value = 2,message = "menuId必须大于1")
    private Long menuId;
    /**
     * 菜单中文名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;
    /**
     * 菜单英文编码
     */
    @NotBlank(message = "菜单英文标识不能为空")
    private String menuCode;
    /**
     * 父级菜单
     */
    @NotNull(message = "父级菜单不能为空")
    private Long parentId;
    /**
     * 路由地址
     */
    private String menuUrl;
    /**
     * 菜单图标
     */
    private String menuIcon;
    /**
     * 菜单排序
     */
    private Integer menuSort;
    /**
     * 描述
     */
    private String description;
    /**
     * 菜单状态
     * 0 - 启用
     * 1 - 停用
     */
    private Integer menuStatus;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
