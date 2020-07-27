package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.authcommon.entity.dto.DeptInfoTree;
import cn.whitetown.authcommon.entity.dto.DeptSimpleTree;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.usersecurity.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @GetMapping("/page")
    public ResponseData<ResponsePage<DeptInfoDto>> queryDeptInfos(DeptQuery deptQuery){
        WhiteToolUtil.defaultPage(deptQuery);
        ResponsePage<DeptInfoDto> result = deptService.searchDeptInfos(deptQuery);
        return ResponseData.ok(result);
    }

    /**
     * 查询部门树
     * @return
     */
    @GetMapping("/tree")
    public ResponseData<DeptInfoTree> queryDeptDetailTree(@NotNull(message = "部门ID不能为空") Long deptId,
                                                          @NotNull(message = "部门最低层级不能为空") @Min(value = 1,message = "最低层级为1") Integer lowLevel){
        DeptInfoTree deptInfoTree = deptService.queryDeptDetailTree(deptId,lowLevel);
        return ResponseData.ok(deptInfoTree);
    }

    /**
     * 查询简化部门树
     * @return
     */
    @GetMapping("/simple_tree")
    public ResponseData<DeptSimpleTree> queryDeptTree(@NotNull(message = "部门ID不能为空") Long deptId,
                                                      @NotNull(message = "部门最低层级不能为空") @Min(value = 1,message = "最低层级为1") Integer lowLevel) {
        DeptSimpleTree simpleTree = deptService.querySimpleTree(deptId,lowLevel);
        return ResponseData.ok(simpleTree);
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
            deptInfo.setParentId(AuthConstant.ROOT_DEPT_ID);
        }
        if(deptInfo.getDeptId().equals(deptInfo.getParentId())){
            throw new CustomException(ResponseStatusEnum.DEPT_PARENT_REPEAT);
        }
        deptService.updateDeptInfo(deptInfo);
        return ResponseData.ok();
    }

    /**
     * 分配Boss信息
     * @param deptId
     * @param username
     * @return
     */
    @GetMapping("/boss")
    public ResponseData configureBoss(@NotNull(message = "部门ID不能为空") @Min(value = 2,message = "非法操作顶层部门") Long deptId,
                                      @NotBlank(message = "用户名不能为空") String username){
        deptService.configureBoss(deptId,username);
        return ResponseData.ok();
    }

    /**
     * 部门状态变更
     * @param deptId
     * @param deptStatus
     * @return
     */
    @GetMapping("/status")
    public ResponseData updateDeptStatus(@NotNull @Min(value = 2,message = "禁止操作ROOT层级部门") Long deptId,
                                     @NotNull @Min(0) @Max(2) Integer deptStatus){
        deptService.updateDeptStatus(deptId,deptStatus);
        return ResponseData.ok();
    }
}
