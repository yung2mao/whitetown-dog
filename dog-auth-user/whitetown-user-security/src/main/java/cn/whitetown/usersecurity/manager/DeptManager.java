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
}
