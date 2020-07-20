package cn.whitetown.usersecurity.manager.impl;

import cn.whitetown.authcommon.entity.po.PositionInfo;
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
    public PositionInfo queryPositionByIdAndDeptCode(Long positionId, String deptCode) {
        if(positionId==null || DataCheckUtil.checkTextNullBool(deptCode)) {
            return null;
        }
        LambdaQueryWrapper<PositionInfo> queryCondition = conditionFactory.getQueryCondition(PositionInfo.class);
        queryCondition.eq(PositionInfo::getDeptCode,deptCode)
                .eq(PositionInfo::getPositionId,positionId);
        return positionMapper.selectOne(queryCondition);
    }
}
