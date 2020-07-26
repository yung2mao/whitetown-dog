package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.authcommon.entity.dto.DeptInfoTree;
import cn.whitetown.authcommon.entity.dto.DeptSimpleDto;
import cn.whitetown.authcommon.entity.dto.DeptSimpleTree;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author taixian
 * @date 2020/07/16
 **/
public interface DeptService extends IService<DeptInfo> {
    /**
     * 部门信息检索
     * @param deptQuery
     * @return
     */
    ResponsePage<DeptInfoDto> searchDeptInfos(DeptQuery deptQuery);

    /**
     * 获取部门信息的简化信息
     * @param deptId
     * @param lowLevel
     * @return
     */
    DeptInfoTree queryDeptDetailTree(Long deptId, Integer lowLevel);

    /**
     * 查询返回简化部门树
     * @param deptId
     * @param lowLevel
     * @return
     */
    DeptSimpleTree querySimpleTree(Long deptId, Integer lowLevel);

    /**
     * 添加部门信息
     * @param deptInfo
     */
    void addDeptInfo(DeptInfo deptInfo);

    /**
     * 部门信息更新
     * @param deptInfo
     */
    void updateDeptInfo(DeptInfo deptInfo);

    /**
     * boss信息分配
     * @param deptId
     * @param username
     */
    void configureBoss(Long deptId,String username);

    /**
     * 部门状态变更
     * @param deptId
     * @param deptStatus
     */
    void updateDeptStatus(Long deptId, Integer deptStatus);
}
