package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;

/**
 * @author taixian
 * @date 2020/07/16
 **/
public interface DeptService {
    /**
     * 部门信息检索
     * @param deptQuery
     * @return
     */
    ResponsePage<DeptInfoDto> searchDeptInfos(DeptQuery deptQuery);
}
