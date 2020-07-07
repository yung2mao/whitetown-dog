package cn.whitetown.usersecurity.manager;

import cn.whitetown.authcommon.entity.po.UserRole;

import java.util.List;

/**
 * 角色管理通用接口
 * @author GrainRain
 * @date 2020/07/07 22:10
 **/
public interface RoleManager {

    /**
     * 根据角色ID查询角色信息
     * @param roleId
     * @return
     */
    UserRole queryRoleById(Long roleId);

    /**
     * 根据角色名称查询角色信息
     * @param roleName
     * @return
     */
    UserRole queryRoleByRoleName(String roleName);

    /**
     * 查询用户的全部角色信息
     * @param userId
     * @return
     */
    List<UserRole> queryRolesByUserId(Long userId);
}
