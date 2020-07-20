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
     * @param deptCode
     * @return
     */
    PositionInfo queryPositionByIdAndDeptCode(Long positionId, String deptCode);
}
