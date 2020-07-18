package cn.whitetown.authcommon.entity.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单树形结构
 * @author GrainRain
 * @date 2020/06/25 10:51
 **/
@Getter
@Setter
public class MenuTree {
    /**
     * 菜单Id
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单英文标识
     */
    private String menuCode;
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
     * 菜单描述
     */
    private String description;
    /**
     * 菜单状态
     */
    private Integer menuStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date createTime;
    /**
     * 子菜单列表
     */
    private List<MenuTree> children = new ArrayList<>();

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
