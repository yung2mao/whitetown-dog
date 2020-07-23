package cn.whitetown.usersecurity.manager;

import cn.whitetown.authcommon.entity.po.DeptInfo;

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
     * 部门表数据更新
     * @param positionId
     * @param userId
     * @param realName
     */
    void updatePositionInfo(Long positionId, Long userId, String realName);
}
