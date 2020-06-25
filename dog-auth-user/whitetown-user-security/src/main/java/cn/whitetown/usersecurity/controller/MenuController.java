package cn.whitetown.usersecurity.controller;

import cn.whitetown.dogbase.common.entity.vo.ResponseData;
import cn.whitetown.usersecurity.entity.po.MenuInfo;
import cn.whitetown.usersecurity.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 菜单管理
 * @author GrainRain
 * @date 2020/06/23 22:41
 **/
@RestController
@RequestMapping("/menu")
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
}
