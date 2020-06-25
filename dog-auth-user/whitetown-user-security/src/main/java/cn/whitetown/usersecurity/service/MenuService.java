package cn.whitetown.usersecurity.service;

import cn.whitetown.usersecurity.entity.po.MenuInfo;

/**
 * 菜单管理服务
 * @author GrainRain
 * @date 2020/06/24 22:22
 **/
public interface MenuService {
    /**
     * 添加一个菜单
     * @param menuInfo
     */
    void addSingleMenu(MenuInfo menuInfo);
}
