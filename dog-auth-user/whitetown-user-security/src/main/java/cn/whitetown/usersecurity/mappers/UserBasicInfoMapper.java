package cn.whitetown.usersecurity.mappers;

import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 用户基本信息处理mapper
 * @author GrainRain
 * @date 2020/06/13 15:11
 **/
public interface UserBasicInfoMapper extends BaseMapper<UserBasicInfo> {
    /**
     * 根据userId获取角色信息
     * @param userId
     * @return
     */
    List<UserRole> selectUserRole(Long userId);

    /**
     * 用户版本信息更新
     * @param username
     */
    void updateUserVersionByUsername(String username);
}
