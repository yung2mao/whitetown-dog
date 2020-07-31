package cn.whitetown.usersecurity.controller;

import cn.whitetown.authcommon.entity.ao.MenuInfoAo;
import cn.whitetown.authcommon.entity.ao.RoleMenuConfigure;
import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.authea.annotation.WhiteAuthAnnotation;
import cn.whitetown.authea.modo.WhiteControlType;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.authcommon.entity.dto.MenuTree;
import cn.whitetown.usersecurity.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * 菜单管理
 * @author GrainRain
 * @date 2020/06/23 22:41
 **/
@RestController
@RequestMapping("/menu")
@Validated
@WhiteAuthAnnotation(type = WhiteControlType.HAS_AUTHORITY,value = "auth_menu")
public class MenuController {

    @Autowired
    private MenuService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 查询指定菜单层级范围内的所有菜单信息，以树形结构返回
     * 包含锁定和正常状态的菜单项
     * @return
     */
    @GetMapping("/tree")
    public ResponseData<MenuTree> queryMenuTree(@NotNull Long menuId,
                                                @NotNull @Min(0) @Max(100) Integer lowLevel){
        MenuTree menuTree = service.queryMenuTree(menuId,lowLevel);
        return ResponseData.ok(menuTree);
    }

    /**
     * 查询当前登录用户可以查看的菜单项
     * @return
     */
    @GetMapping("/login_menu")
    public ResponseData<MenuTree> queryActiveMenuTree(@NotNull(message = "菜单ID不能为空") Long menuId,@NotNull(message = "最低层级不能为空") Integer lowLevel){
        Long userId = jwtTokenUtil.getUserId();
        MenuTree menuTree = service.queryActiveMenuByUserId(userId,menuId,lowLevel);
        return ResponseData.ok(menuTree);
    }

    /**
     * 根据角色查询绑定的菜单信息
     * @param roleName
     * @return
     */
    @GetMapping("/role")
    public ResponseData<MenuTree> queryUserMenuTree(@NotBlank(message = "角色名称不能为空") String roleName){
        MenuTree menuTree = service.queryMenuTreeByRoleName(roleName);
        return ResponseData.ok(menuTree);
    }

    /**
     * 根据roleId查询绑定菜单的Id信息
     * @param roleId
     * @return
     */
    @GetMapping("/role_menuIds")
    public ResponseData<List<Long>> getMenuIdsByRoleId(@NotNull Long roleId){
        List<Long> ids = service.queryMenuIdsByRoleId(roleId);
        return ResponseData.ok(ids);
    }

    /**
     * 新增菜单信息
     * @param menuInfo
     * @return
     */
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResponseData addMenu(@RequestBody @Valid MenuInfoAo menuInfo){
        service.addSingleMenu(jwtTokenUtil.getUserId(),menuInfo);
        return ResponseData.ok();
    }

    /**
     * 菜单信息更新
     * @param menuInfo
     * @return
     */
    @PostMapping(value = "update", produces = "application/json;charset=UTF-8")
    public ResponseData updateMenuInfo(@RequestBody @Valid MenuInfoAo menuInfo){
        if(menuInfo.getMenuId()==null){
            return ResponseData.build(400,"ID不能为空",null);
        }
        if(menuInfo.getMenuId().equals(menuInfo.getParentId())){
            return ResponseData.build(400,"父级菜单不能等于自身",null);
        }
        service.updateMenuInfo(jwtTokenUtil.getUserId(),menuInfo);
        return ResponseData.ok();
    }

    /**
     * 菜单状态变更
     * 0 - 活跃
     * 1 - 锁定
     * 2 - 删除
     * @param menuId
     * @param menuStatus
     * @return
     */
    @GetMapping("status")
    public ResponseData updateMenuStatus(@NotNull(message = "菜单ID不能为空") @Min(value = 2,message = "禁止操作顶级菜单") Long menuId, @NotNull @Min(0) @Max(2) Integer menuStatus){
        service.updateMenuStatus(menuId,menuStatus);
        return ResponseData.ok();
    }
    /**
     * 角色与菜单数据绑定
     * @return
     */
    @PostMapping(value = "role_menu",produces = "application/json;charset=UTF-8")
    public ResponseData updateRoleMenu(@RequestBody @Valid RoleMenuConfigure configure){
        service.updateRoleMenus(configure);
        return ResponseData.ok();
    }
}
