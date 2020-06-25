package cn.whitetown.usersingle.mappers;

import cn.whitetown.authcommon.entity.UserRole;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author taixian
 */
public interface UserBasicInfoMapper extends BaseMapper<UserBasicInfo> {
    /**
     * 通过用户名搜索用户信息
     * @param username
     * @return
     */
    UserBasicInfo selectUserByUsername(String username);

    /**
     * 通过userId搜索用户的角色信息
     * @param userId
     * @return
     */
    List<UserRole> selectUserRole(Long userId);

}