package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.ao.RoleQuery;
import cn.whitetown.authcommon.entity.ao.UserRoleConfigure;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.dto.RoleInfoDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色管理服务
 * @author GrainRain
 * @date 2020/06/28 21:53
 **/
public interface RoleManageService extends IService<UserRole> {

    /**
     * 查询所有角色信息
     * @return
     */
    List<RoleInfoDto> queryAllRoles();

    /**
     * 根据用户名查询用户角色信息
     * @param username
     * @return
     */
    List<RoleInfoDto> queryRolesByUsername(String username);

    /**
     * 搜索角色信息
     * @param roleQuery
     * @return
     */
    List<RoleInfoDto> searchRole(RoleQuery roleQuery);

    /**
     * 添加角色信息
     * @param role
     */
    void addRole(RoleInfoDto role);

    /**
     * 更新角色信息
     * @param role
     */
    void updateRoleInfo(RoleInfoDto role);

    /**
     * 角色状态更新
     * @param roleId
     * @param roleStatus
     */
    void updateRoleStatus(Long roleId, Integer roleStatus);

    /**
     * 用户角色配置
     * @param roleConfigureAo
     */
    void updateUserRoleRelation(UserRoleConfigure roleConfigureAo);
}
