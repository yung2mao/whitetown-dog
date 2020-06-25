package cn.whitetown.usersecurity.entity.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 菜单信息
 * @author GrainRain
 * @date 2020/06/24 22:14
 **/
@Getter
@Setter
@TableName("sys_menu")
public class MenuInfo {
    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
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
    private Long parentId;
    /**
     * 路由地址
     */
    @NotBlank(message = "路由地址不能为空")
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
     * 第几级菜单
     * 0 - 顶级
     * 100 - 最底级，直接操作的按钮类别
     */
    @NotNull(message = "菜单级别不能为空")
    private Integer menuLevel;
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
    /**
     * 创建人ID
     */
    private Long createUserId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新人ID
     */
    private Long updateUserId;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
