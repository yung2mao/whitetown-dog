package cn.whitetown.usersecurity.mappers;

import cn.whitetown.authcommon.entity.po.MenuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import javax.validation.constraints.Pattern;
import java.util.List;

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
    List<MenuInfo> selectMenuByRoleId(Long roleId);
}
