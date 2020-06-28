package cn.whitetown.authcommon.entity.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 角色信息展示数据
 * @author GrainRain
 * @date 2020/06/28 21:45
 **/
@Getter
@Setter
public class RoleInfoVo {
    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Pattern(regexp = "^[a-zA-Z_]+$",message = "只能由字母和下划线组成")
    private String name;
    /**
     * 角色描述
     */
    @NotBlank(message = "角色描述不能为空")
    private String description;
    /**
     * 排序 - 如果为空顺次排列
     */
    private Integer sort;
    /**
     * 角色状态
     */
    private Integer roleStatus;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 最后更新
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
