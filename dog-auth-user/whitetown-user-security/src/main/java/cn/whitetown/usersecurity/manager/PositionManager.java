package cn.whitetown.usersecurity.manager;

import cn.whitetown.authcommon.entity.po.PositionInfo;

/**
 * @author taixian
 * @date 2020/07/20
 **/
public interface PositionManager {
    /**
     * 查询职位信息
     * @param positionId
     * @param deptId
     * @return
     */
    PositionInfo queryPositionByIdAndDeptId(Long deptId, Long positionId);

    /**
     * 根据ID检索职位信息
     * @param bossPositionId
     * @return
     */
    PositionInfo queryPositionById(Long bossPositionId);
}
