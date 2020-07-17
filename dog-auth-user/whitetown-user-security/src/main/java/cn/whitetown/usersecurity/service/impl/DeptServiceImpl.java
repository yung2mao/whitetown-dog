package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.usersecurity.service.DeptService;
import org.springframework.stereotype.Service;

/**
 * 部门管理
 * @author taixian
 * @date 2020/07/16
 **/
@Service
public class DeptServiceImpl implements DeptService {
    /**
     * 部门信息检索
     * @param deptQuery
     * @return
     */
    @Override
    public ResponsePage<DeptInfoDto> searchDeptInfos(DeptQuery deptQuery) {
        return null;
    }
}
