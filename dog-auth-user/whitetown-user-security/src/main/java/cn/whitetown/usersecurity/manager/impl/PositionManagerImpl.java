package cn.whitetown.usersecurity.manager.impl;

import cn.whitetown.authcommon.entity.po.PositionInfo;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.PositionManager;
import cn.whitetown.usersecurity.mappers.PositionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 职位信息通用管理类
 * @author taixian
 * @date 2020/07/20
 **/
@Service
public class PositionManagerImpl implements PositionManager {
    @Resource
    private PositionMapper positionMapper;

    @Autowired
    private QueryConditionFactory conditionFactory;

    @Override
    public PositionInfo queryPositionByIdAndDeptId(Long deptId, Long positionId) {
        if(positionId==null || deptId == null) {
            return null;
        }
        LambdaQueryWrapper<PositionInfo> queryCondition = conditionFactory.getQueryCondition(PositionInfo.class);
        queryCondition.eq(PositionInfo::getDeptId,deptId)
                .eq(PositionInfo::getPositionId,positionId)
                .in(PositionInfo::getPositionStatus,DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN);
        return positionMapper.selectOne(queryCondition);
    }

    @Override
    public PositionInfo queryPositionById(Long bossPositionId) {
        if(bossPositionId == null) {
            return null;
        }
        LambdaQueryWrapper<PositionInfo> queryWrapper = conditionFactory.getQueryCondition(PositionInfo.class)
                .eq(PositionInfo::getPositionId, bossPositionId)
                .in(PositionInfo::getPositionStatus, DogBaseConstant.ACTIVE_NORMAL, DogBaseConstant.DISABLE_WARN);
        return positionMapper.selectOne(queryWrapper);
    }
}
