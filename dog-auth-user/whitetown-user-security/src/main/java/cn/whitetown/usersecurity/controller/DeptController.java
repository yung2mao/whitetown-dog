package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.authcommon.entity.dto.DeptSimpleDto;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.usersecurity.service.DeptService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 部门管理
 * @author taixian
 * @date 2020/07/15
 **/
@RestController
@RequestMapping("/dept")
@Validated
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 部门信息检索
     * @param deptQuery
     * @return
     */
    @GetMapping("/pageDept")
    public ResponseData<ResponsePage<DeptInfoDto>> queryDeptInfos(DeptQuery deptQuery){
        WhiteToolUtil.defaultPage(deptQuery);
        ResponsePage<DeptInfoDto> result = deptService.searchDeptInfos(deptQuery);
        return ResponseData.ok(result);
    }

    /**
     * 获取所有部门的简化基本信息
     * @return
     */
    @GetMapping("allSimple")
    public ResponseData<List<DeptSimpleDto>> queryAllSimpleDept(){
        List<DeptSimpleDto> deptSimpleDtoList = deptService.searchAllSimpleDept();
        return ResponseData.ok(deptSimpleDtoList);
    }

    /**
     * 添加部门信息
     * @param deptInfo
     * @return
     */
    @PostMapping(value = "/add",produces = "application/json;charset=UTF-8")
    public ResponseData addDeptInfo(@RequestBody @Valid DeptInfo deptInfo){
        deptService.addDeptInfo(deptInfo);
        return ResponseData.ok();
    }

    /**
     * 部门信息更新
     * @param deptInfo
     * @return
     */
    @PostMapping("/update")
    public ResponseData updateDeptInfo(@RequestBody @Valid DeptInfo deptInfo){
        if(deptInfo.getDeptId()==null){
            throw new CustomException(ResponseStatusEnum.NO_THIS_DEPT);
        }
        if(deptInfo.getParentId() == null){
            throw new CustomException(ResponseStatusEnum.NO_PARENT_DEPT);
        }
        if(deptInfo.getDeptId().equals(deptInfo.getParentId())){
            throw new CustomException(ResponseStatusEnum.DEPT_PARENT_REPEAT);
        }
        deptService.updateDeptInfo(deptInfo);
        return ResponseData.ok();
    }

    /**
     * 部门状态变更
     * @param deptId
     * @param deptStatus
     * @return
     */
    @GetMapping("/status")
    public ResponseData updateStatus(@NotNull @Min(value = 2,message = "禁止操作ROOT层级部门") Long deptId,
                                     @NotNull @Min(0) @Max(2) Integer deptStatus){
        deptService.updateDeptStatus(deptId,deptStatus);
        return ResponseData.ok();
    }
}
