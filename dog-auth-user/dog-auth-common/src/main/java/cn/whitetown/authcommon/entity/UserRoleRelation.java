package cn.whitetown.authcommon.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author GrainRain
 * @date 2020/06/17 21:54
 **/
@Setter
@Getter
@TableName("user_role_relation")
public class UserRoleRelation {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;

    public UserRoleRelation() {
    }

    public UserRoleRelation(Long id, Long userId, Long roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
