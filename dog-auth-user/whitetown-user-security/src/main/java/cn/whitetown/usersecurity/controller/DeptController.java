package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.usersecurity.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门管理
 * @author taixian
 * @date 2020/07/15
 **/
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 部门信息检索
     * @param deptQuery
     * @return
     */
    @RequestMapping("/pageDept")
    public ResponseData<ResponsePage<DeptInfoDto>> queryDeptInfos(DeptQuery deptQuery){
        WhiteToolUtil.defaultPage(deptQuery);
        ResponsePage<DeptInfoDto> result = deptService.searchDeptInfos(deptQuery);
        return ResponseData.ok(result);
    }
}
