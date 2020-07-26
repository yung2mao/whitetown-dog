package cn.whitetown.usersecurity.mappers;

import cn.whitetown.authcommon.entity.po.DeptInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author taixian
 * @date 2020/07/18
 **/
public interface DeptInfoMapper extends BaseMapper<DeptInfo> {

    /**
     * 查询部门信息-获取部门树
     * @param deptId
     * @param lowLevel
     * @return
     */
    List<DeptInfo> selectDeptTreeList(@Param("deptId") Long deptId, @Param("lowLevel") Integer lowLevel);

    /**
     * 删除- 处理部门关联的相关信息
     * @param deptId
     * @param deptStatus
     */
    void delDeptStatusAndRelationSystem(@Param("deptId") Long deptId,@Param("deptStatus") Integer deptStatus);

    /**
     * 清理 - 如果用户表信息与更新不符,则执行清理
     * @param deptId
     * @param positionId
     * @param userId
     */
    void updateUserDeptInfoIfExists(@Param("deptId") Long deptId,@Param("positionId") Long positionId,@Param("userId") Long userId);

    /**
     * 部门的BOSS信息更新
     * @param positionId
     * @param userId
     * @param realName
     */
    void updateDeptBossInfo(@Param("positionId") Long positionId,@Param("userId") Long userId,@Param("bossName") String realName);
}
