package cn.whitetown.usersingle.mappers;

import cn.whitetown.dogbase.user.entity.UserBasicInfo;
import cn.whitetown.dogbase.user.entity.UserRole;

import java.util.List;

public interface UserBasicInfoMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(UserBasicInfo record);

    int insertSelective(UserBasicInfo record);

    UserBasicInfo selectByPrimaryKey(UserBasicInfo userBasicInfo);

    UserBasicInfo selectUserByUsername(String username);

    int updateByPrimaryKeySelective(UserBasicInfo record);

    int updateByPrimaryKey(UserBasicInfo record);

    List<UserRole> selectUserRole(List<Long> roleIds);

}