package cn.whitetown.usersecurity.mappers;

import cn.whitetown.authcommon.entity.UserRoleRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * user信息和role信息关系映射表数据库操作
 * @author GrainRain
 * @date 2020/06/17 21:57
 **/
public interface UserRoleRelationMapper extends BaseMapper<UserRoleRelation> {
    /**
     * 删除角色涉及的关联关系
     * @param roleId
     */
    void removeRoleRelation(Long roleId);

    /**
     * 删除用户关联的角色信息
     * @param userId
     */
    void removeRoleRelationByUserId(Long userId);

    /**
     * 用户与角色关联关系更新
     * @param userId
     * @param roleIds
     */
    void updateUserRoleRelation(@Param("userId") Long userId,@Param("roleIds") List<Long> roleIds);
}
