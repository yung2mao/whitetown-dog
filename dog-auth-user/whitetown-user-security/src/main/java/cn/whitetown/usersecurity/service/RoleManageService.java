package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.vo.RoleInfoVo;
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
    List<RoleInfoVo> queryAllRoles();

    /**
     * 添加角色信息
     * @param role
     */
    void addRole(RoleInfoVo role);

    /**
     * 更新角色信息
     * @param role
     */
    void updateRoleInfo(RoleInfoVo role);
}
