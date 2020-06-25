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
     * @return
     */
    @GetMapping("/tree")
    public ResponseData<MenuTree> queryMenuTree(@NotBlank String menuCode,
                                                @NotNull @Min(0) @Max(100) Integer lowLevel){
        MenuTree menuTree = service.queryMenuTree(menuCode,lowLevel);
        return ResponseData.ok(menuTree);
    }

    @PostMapping(value = "update",produces = "application/json;charset=UTF-8")
    public ResponseData updateMenu(@RequestBody @Valid MenuInfo menuInfo){
        if(menuInfo.getMenuId()==null){
            throw new RuntimeException("ID不能为空");
        }
        return ResponseData.ok();
    }
}
