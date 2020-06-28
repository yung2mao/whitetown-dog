package cn.whitetown.authcommon.entity.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户角色信息
 * @author GrainRain
 * @date 2020/05/30 15:39
 **/
@Setter
@Getter
@TableName("user_role")
public class UserRole {
    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 序号
     */
    private Integer sort;
    /**
     * 角色状态
     * 0 - 正常
     * 1 - 锁定
     * 2 - 删除
     */
    private Integer roleStatus;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 创建人id
     */
    private Long createUserId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新人id
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
