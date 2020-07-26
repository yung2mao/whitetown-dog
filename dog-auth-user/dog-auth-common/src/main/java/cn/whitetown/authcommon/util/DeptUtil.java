package cn.whitetown.authcommon.util;

import cn.whitetown.authcommon.entity.dto.DeptInfoTree;
import cn.whitetown.authcommon.entity.dto.DeptSimpleTree;
import cn.whitetown.authcommon.entity.po.DeptInfo;

import java.util.List;

/**
 * @author taixian
 * @date 2020/07/25
 **/
public interface DeptUtil {
    /**
     * 构建DeptTree
     * @param deptInfos
     * @return
     */
    DeptSimpleTree createDeptSimpleTree(List<DeptInfo> deptInfos);

    /**
     * 构建部门详情树形
     * @param deptInfos
     * @return
     */
    DeptInfoTree createDeptDetailTree(List<DeptInfo> deptInfos);
}
