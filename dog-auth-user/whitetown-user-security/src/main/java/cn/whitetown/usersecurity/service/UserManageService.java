package cn.whitetown.usersecurity.service;

import cn.whitetown.dogbase.common.entity.vo.ResponsePage;
import cn.whitetown.dogbase.user.entity.po.UserBasicInfo;
import cn.whitetown.usersecurity.entity.ao.UserBasicQuery;
import cn.whitetown.usersecurity.entity.vo.UserBasicInfoVo;
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
    ResponsePage<UserBasicInfoVo> queryUserBasicList(UserBasicQuery userQuery);

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
    void retryPassword(String username);

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

}
