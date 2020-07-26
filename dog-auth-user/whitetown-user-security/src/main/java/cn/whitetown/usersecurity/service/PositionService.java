package cn.whitetown.usersecurity.service;

import cn.whitetown.authcommon.entity.ao.PositionQuery;
import cn.whitetown.authcommon.entity.dto.PositionDto;
import cn.whitetown.authcommon.entity.po.PositionInfo;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 职位管理服务
 * @author taixian
 * @date 2020/07/20
 **/
public interface PositionService extends IService<PositionInfo> {
    /**
     * 分页查询职位信息
     * @param positionQuery
     * @return
     */
    ResponsePage<PositionDto> queryPagePositions(PositionQuery positionQuery);

    /**
     * 根据deptCode查询职位信息
     * @param deptId
     * @return
     */
    List<PositionDto> queryDeptPosition(Long deptId);

    /**
     * 添加职位信息
     * @param position
     */
    void addPosition(PositionInfo position);

    /**
     * 更新职位信息
     * @param position
     */
    void updatePosition(PositionInfo position);

    /**
     * 职位状态更新
     * @param positionId
     * @param status
     */
    void updatePositionStatus(Long positionId, int status);
}
