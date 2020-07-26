package cn.whitetown.usersecurity.manager;

import cn.whitetown.authcommon.entity.po.DeptInfo;

import java.util.List;

/**
 * @author taixian
 * @date 2020/07/20
 **/
public interface DeptManager {

    /**
     * 根据部门ID查询部门信息
     * @param deptId
     * @return
     */
    DeptInfo queryDeptInfoById(Long deptId);

    /**
     * 查询构建部门树所需信息
     * @param deptId
     * @param lowLevel
     * @return
     */
    List<DeptInfo> queryDeptInfoTreeList(Long deptId,Integer lowLevel);

    /**
     * 部门表数据更新
     * @param positionId
     * @param userId
     * @param realName
     */
    void updatePositionInfo(Long positionId, Long userId, String realName);
}
