package cn.whitetown.usersecurity.manager;

import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

/**
 * 用户管理通用接口
 * @author GrainRain
 * @date 2020/07/07 21:56
 **/
public interface UserManager {

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    UserBasicInfo getUserByUsername(String username);

    /**
     * 条件查询所有满足条件的user
     * @param queryWrapper
     * @return
     */
    List<UserBasicInfo> getUserByWrapper(LambdaQueryWrapper<UserBasicInfo> queryWrapper);
}
