package cn.whitetown.usersecurity.mappers;

import cn.whitetown.authcommon.entity.po.PositionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author taixian
 * @date 2020/07/20
 **/
public interface PositionMapper extends BaseMapper<PositionInfo> {
    /**
     * 职位相关表更新
     * @param position
     */
    void updatePositionInfo(PositionInfo position);

    /**
     * 更新关联表position信息为null
     * @param positionId
     */
    void updatePositionWithNull(Long positionId);
}
