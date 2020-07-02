package cn.whitetown.usersecurity.controller;

import cn.whitetown.dogbase.common.entity.vo.ResponseData;
import cn.whitetown.authcommon.entity.po.MenuInfo;
import cn.whitetown.authcommon.entity.vo.MenuTree;
import cn.whitetown.usersecurity.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 菜单管理
 * @author GrainRain
 * @date 2020/06/23 22:41
 **/
@RestController
@RequestMapping("/menu")
@Validated
public class MenuController {

    @Autowired
    private MenuService service;
    /**
     * 新增菜单信息
     * @param menuInfo
     * @return
     */
    @PostMapping(value = "/add",produces = "application/json;charset=UTF-8")
    public ResponseData addMenu(@RequestBody @Valid MenuInfo menuInfo){
        service.addSingleMenu(menuInfo);
        return ResponseData.ok();
    }

    /**
     * 查询指定菜单层级范围内的所有菜单信息，以树形结构返回
     * 包含锁定和正常状态的菜单项
     * @return
     */
    @GetMapping("/tree")
    public ResponseData<MenuTree> queryMenuTree(@NotBlank String menuCode,
                                                @NotNull @Min(0) @Max(100) Integer lowLevel){
        MenuTree menuTree = service.queryMenuTree(menuCode,lowLevel);
        return ResponseData.ok(menuTree);
    }

    /**
     * 查询当前登录用户可以查看的菜单项
     * @return
     */
    @GetMapping("/loginMenu")
    public ResponseData<MenuTree> queryActiveMenuTree(){
        return null;
    }

    /**
     * 根据角色查询绑定的菜单信息
     * @param roleName
     * @return
     */
    @GetMapping("roleMenu")
    public ResponseData<MenuTree> queryUserMenuTree(String roleName){
        return null;
    }

    /**
     * 菜单信息更新
     * @param menuInfo
     * @return
     */
    @PostMapping(value = "update",produces = "application/json;charset=UTF-8")
    public ResponseData updateMenu(@RequestBody @Valid MenuInfo menuInfo){
        if(menuInfo.getMenuId()==null){
            throw new RuntimeException("ID不能为空");
        }

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
    public ResponseData updateMenuStatus(@NotNull(message = "菜单ID不能为空") Long menuId,@NotNull @Min(0) @Max(2) Integer menuStatus){
        service.updateMenuStatus(menuId,menuStatus);
        return ResponseData.ok();
    }
}
