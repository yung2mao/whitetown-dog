package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.entity.ao.UserRoleConfigureAo;
import cn.whitetown.authcommon.entity.po.UserRole;
import cn.whitetown.authcommon.entity.vo.RoleInfoVo;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.entity.vo.ResponseData;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.usersecurity.service.RoleManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色管理
 * @author GrainRain
 * @date 2020/06/28 21:36
 **/
@RestController
@RequestMapping("/role")
@Validated
public class RoleManageController {

    @Autowired
    private RoleManageService service;

    /**
     * 获取所有角色信息
     * @return
     */
    @GetMapping("/getAll")
    public ResponseData<List<RoleInfoVo>> queryAllRoles(){
        List<RoleInfoVo> roleInfoList =  service.queryAllRoles();
        return ResponseData.ok(roleInfoList);
    }

    /**
     * 查询单个用户的角色信息
     * @return
     */
    @GetMapping("/one")
    public ResponseData<List<RoleInfoVo>> queryUserRoleByUsername(@NotBlank(message = "用户名不能为空") String username){
        List<RoleInfoVo> roleInfoVoList = service.queryRolesByUsername(username);
        return ResponseData.ok(roleInfoVoList);
    }

    /**
     * 新增角色
     * id、roleStatus不传值
     * @return
     */
    @GetMapping("/add")
    public ResponseData addRole(@Valid RoleInfoVo role){
        service.addRole(role);
        return ResponseData.ok();
    }

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    @GetMapping("/update")
    public ResponseData updateRole(@Valid RoleInfoVo role){
        if(role.getRoleId() == null){
            throw new CustomException(ResponseStatusEnum.ERROR_PARAMS);
        }
        service.updateRoleInfo(role);
        return ResponseData.ok();
    }

    /**
     * 角色状态变更
     * @param roleId
     * @param roleStatus
     * @return
     */
    @GetMapping("/status")
    public ResponseData updateUserStatus(@NotBlank String roleId,@NotNull @Min(0) @Max(2) Integer roleStatus){
        return null;
    }


    /**
     * 用户角色分配
     * @param roleConfigureAo
     * @return
     */
    @PostMapping("/configureRole")
    public ResponseData configureUserRole(UserRoleConfigureAo roleConfigureAo){
        return null;
    }
}
