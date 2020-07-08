package cn.whitetown.usersecurity.mappers;

import cn.whitetown.authcommon.entity.po.MenuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

/**
 * 菜单信息数据库操作
 * @author GrainRain
 * @date 2020/06/24 22:29
 **/
public interface MenuInfoMapper extends BaseMapper<MenuInfo> {

    /**
     * 搜索满足条件的所有菜单
     * @param menuCode
     * @param lowLevel
     * @return
     */
    List<MenuInfo> selectMenuListByCodeAndLevel(@Param("menuCode") String menuCode,@Param("lowLevel") Integer lowLevel);

    /**
     * 根据用户ID搜索具有访问权限的活跃菜单项
     * @param menuStatus
     * @param userId
     * @return
     */
    List<MenuInfo> selectActiveMenuByUserId(@Param("menuStatus") Integer menuStatus,@Param("userId") Long userId);

    /**
     * 根据角色ID查询菜单信息
     * @param roleId
     * @return
     */
    List<MenuInfo> selectMenuByRoleId(@P("roleId") Long roleId);

    /**
     * 更新角色与菜单的绑定信息
     * @param roleId
     * @param menuSet
     */
    void updateRoleMenus(@Param("roleId") Long roleId,@Param("menuIds") Set menuSet);

    /**
     * 移除菜单与角色绑定的关联关系
     * @param menuId
     */
    void removeRelationByMenuId(@Param("menuId") Long menuId);
}
