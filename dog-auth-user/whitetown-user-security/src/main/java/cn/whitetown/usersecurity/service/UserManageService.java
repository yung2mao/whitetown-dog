package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.ao.RoleUserQuery;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.authcommon.entity.ao.UserBasicQuery;
import cn.whitetown.authcommon.entity.dto.UserBasicInfoDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户管理服务
 * @author GrainRain
 * @date 2020/06/17 20:48
 **/
public interface UserManageService extends IService<UserBasicInfo> {

    /**
     * 条件查询用户信息
     * @param userQuery
     * @return
     */
    ResponsePage<UserBasicInfoDto> queryUserBasicList(UserBasicQuery userQuery);

    /**
     * 根据角色ID查询绑定的用户信息
     * @param roleUserQuery
     * @return
     */
    ResponsePage<UserBasicInfoDto> queryUserByRoleId(RoleUserQuery roleUserQuery);

    /**
     * 分配用户/注册用户
     * @param username
     * @param password
     * @param roleName
     */
    void addUserBasicInfo(String username, String password, String roleName);

    /**
     * 用户信息维护
     * @param userInfo
     */
    void updateUser(UserBasicInfo userInfo);

    /**
     * 密码重置操作
     * @param username
     */
    void reSetPassword(String username);

    /**
     * 校验原有密码是否正确
     * @param username
     * @param password
     * @return
     */
    String checkPassword(String username,String password);

    /**
     * 密码更新操作
     * @param username
     * @param pwdToken
     * @param newPassword
     */
    void updatePassword(String username, String pwdToken, String newPassword);

    /**
     * 修改用户状态
     * 0 - 激活
     * 1 - 停用
     * 2 - 删除
     * @param username
     * @param userStatus
     */
    void changeUserStatus(String username, Integer userStatus);
}
