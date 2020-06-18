package cn.whitetown.usersecurity.service;

import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户管理服务
 * @author GrainRain
 * @date 2020/06/17 20:48
 **/
public interface UserManageService extends IService<UserBasicInfo> {
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
     * 密码更新操作
     * @param username
     * @param oldPassword
     * @param newPassword
     */
    void updatePassword(String username, String oldPassword, String newPassword);
}
