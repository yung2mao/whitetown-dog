package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.ao.PositionQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.authcommon.entity.dto.PositionDto;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.authcommon.entity.po.PositionInfo;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.entity.WhiteLambdaQueryWrapper;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.mappers.PositionMapper;
import cn.whitetown.usersecurity.service.PositionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author taixian
 * @date 2020/07/20
 **/
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, PositionInfo> implements PositionService {
    @Autowired
    private QueryConditionFactory conditionFactory;

    @Resource
    private BeanTransFactory transFactory;
    /**
     * 分页查询职位信息
     * @param positionQuery
     * @return
     */
    @Override
    public ResponsePage<PositionDto> queryPagePositions(PositionQuery positionQuery) {
        LambdaQueryWrapper<PositionInfo> queryWrapper = conditionFactory.allEqWithNull2IsNull(positionQuery, PositionInfo.class)
                .in(PositionInfo::getPositionStatus,DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN);
        WhiteLambdaQueryWrapper<PositionInfo> whiteQueryWrapper = conditionFactory.createWhiteQueryWrapper(queryWrapper);
        queryWrapper = whiteQueryWrapper.between(PositionInfo::getCreateTime,positionQuery.getStartTime(),positionQuery.getEndTime(),false)
                .getLambdaQueryWrapper();
        Page<PositionInfo> page = conditionFactory.createPage(positionQuery.getPage(), positionQuery.getSize(), PositionInfo.class);
        Page<PositionInfo> result = this.page(page, queryWrapper);
        List<PositionDto> list = result.getRecords().stream().map(record -> transFactory.trans(record, PositionDto.class))
                .collect(Collectors.toList());
        return ResponsePage.createPage(result.getCurrent(),result.getSize(),result.getTotal(),list);
    }

    /**
     * 根据deptId查询对应所有职位信息
     * @param deptId
     * @return
     */
    @Override
    public List<PositionDto> queryDeptPosition(Long deptId) {
        LambdaQueryWrapper<PositionInfo> queryCondition = conditionFactory.getQueryCondition(PositionInfo.class);
        queryCondition.eq(PositionInfo::getDeptId,deptId)
                .in(PositionInfo::getPositionStatus,DogBaseConstant.ACTIVE_NORMAL);
        List<PositionInfo> list = this.list(queryCondition);
        List<PositionDto> result = list.stream().sorted(Comparator.comparing(PositionInfo::getPositionSort))
                .map(positionInfo -> transFactory.trans(positionInfo, PositionDto.class)).collect(Collectors.toList());
        return result;
    }
}
