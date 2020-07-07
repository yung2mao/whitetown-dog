package cn.whitetown.usersecurity.mappers;


import cn.whitetown.authcommon.entity.po.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 角色信息数据库操作
 * @author GrainRain
 * @date 2020/06/17 21:49
 **/
public interface RoleInfoMapper extends BaseMapper<UserRole> {
    /**
     * 查询指定用户角色信息
     * @param username
     * @return
     */
    List<UserRole> selectRolesByUsername(String username);

    /**
     * 通过用户ID查询对应的角色列表
     * @param userId
     * @return
     */
    List<UserRole> selectRolesByUserId(Long userId);
}
