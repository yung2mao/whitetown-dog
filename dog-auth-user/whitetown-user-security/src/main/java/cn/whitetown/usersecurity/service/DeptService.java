package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.authcommon.entity.dto.DeptSimpleDto;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * @return
     */
    List<DeptSimpleDto> searchAllSimpleDept();

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
     * 部门状态变更
     * @param deptId
     * @param deptStatus
     */
    void updateDeptStatus(Long deptId, Integer deptStatus);
}
