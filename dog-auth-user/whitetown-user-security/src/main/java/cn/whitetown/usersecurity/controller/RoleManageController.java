package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.entity.ao.RoleQuery;
import cn.whitetown.authcommon.entity.ao.UserRoleConfigure;
import cn.whitetown.authcommon.entity.dto.RoleInfoDto;
import cn.whitetown.authea.annotation.WhiteAuthAnnotation;
import cn.whitetown.authea.modo.WhiteControlType;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.updown.util.ExcelUtil;
import cn.whitetown.usersecurity.downentity.RoleExcelTemplate;
import cn.whitetown.usersecurity.service.RoleManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 角色管理
 * @author GrainRain
 * @date 2020/06/28 21:36
 **/
@RestController
@RequestMapping("/role")
@Validated
@WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "auth_role")
public class RoleManageController {

    @Autowired
    private RoleManageService service;

    private ExcelUtil excelUtil = ExcelUtil.getInstance();

    /**
     * 获取所有角色信息
     * @return
     */
    @GetMapping("/get_all")
    public ResponseData<List<RoleInfoDto>> queryAllRoles(){
        List<RoleInfoDto> roleInfoList =  service.queryAllRoles();
        return ResponseData.ok(roleInfoList);
    }

    /**
     * 查询单个用户的角色信息
     * @return
     */
    @GetMapping("/users")
    public ResponseData<List<RoleInfoDto>> queryUserRoleByUsername(@NotBlank(message = "用户名不能为空") String username){
        List<RoleInfoDto> roleInfoDtoList = service.queryRolesByUsername(username);
        return ResponseData.ok(roleInfoDtoList);
    }

    /**
     * 搜索
     * @param roleQuery
     * @return
     */
    @GetMapping("/search")
    public ResponseData<List<RoleInfoDto>> searchRole(RoleQuery roleQuery){
        List<RoleInfoDto> roleInfos = service.searchRole(roleQuery);
        return ResponseData.ok(roleInfos);
    }

    /**
     * 角色信息下载
     * @param roleQuery
     */
    @GetMapping("/downloads")
    public void download(RoleQuery roleQuery, HttpServletResponse response) {
        List<RoleInfoDto> roleInfos = service.searchRole(roleQuery);
        String fileName = "role_" + WhiteFormatUtil.dateFormat("yyyy-MM-dd",new Date());
        String sheetName = "sheet0";
        try {
            excelUtil.writeWebExcel(response, roleInfos, fileName, sheetName, RoleExcelTemplate.class);
        }catch (Exception e) {
            throw new WhResException(ResponseStatusEnum.DOWN_FILE_ERROR);
        }
    }

    /**
     * 新增角色
     * id、roleStatus不传值
     * @return
     */
    @PostMapping(value = "/add",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "role_add_button")
    public ResponseData addRole(@RequestBody @Valid RoleInfoDto role){
        service.addRole(role);
        return ResponseData.ok();
    }

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    @PostMapping(value = "/update",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "auth_role_update")
    public ResponseData updateRole(@RequestBody @Valid RoleInfoDto role){
        if(role.getRoleId() == null){
            throw new WhResException(ResponseStatusEnum.ERROR_PARAMS);
        }
        service.updateRoleInfo(role);
        return ResponseData.ok();
    }

    /**
     * 角色状态变更
     * 0 - 激活
     * 1 - 锁定
     * 2 - 删除
     * @param roleId
     * @param roleStatus
     * @return
     */
    @GetMapping("/status")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_ANY_AUTHORITY,value = {"auth_role_update","auth_role_del"})
    public ResponseData updateRoleStatus(@NotNull Long roleId,@NotNull @Min(0) @Max(2) Integer roleStatus){
        service.updateRoleStatus(roleId,roleStatus);
        return ResponseData.ok();
    }

    /**
     * 用户角色分配
     * @param roleConfigureAo
     * @return
     */
    @PostMapping(value = "/configure_role",produces = "application/json;charset=UTF-8")
    @WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "user_role_configure")
    public ResponseData configureUserRole(@RequestBody @Valid UserRoleConfigure roleConfigureAo){
        Long[] roleIds = roleConfigureAo.getRoleIds();
        for(Long id:roleIds){
            if(id == null || id < 1){
                throw new WhResException(ResponseStatusEnum.REQUEST_INVALIDATE);
            }
        }
        service.updateUserRoleRelation(roleConfigureAo);
        return ResponseData.ok();
    }
}
