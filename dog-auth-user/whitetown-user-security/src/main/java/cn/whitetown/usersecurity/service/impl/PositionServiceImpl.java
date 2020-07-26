package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.ao.PositionQuery;
import cn.whitetown.authcommon.entity.dto.PositionDto;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.authcommon.entity.po.PositionInfo;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.db.entity.WhiteLambdaQueryWrapper;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.DeptManager;
import cn.whitetown.usersecurity.mappers.PositionMapper;
import cn.whitetown.usersecurity.service.PositionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.DataOutput;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author taixian
 * @date 2020/07/20
 **/
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, PositionInfo> implements PositionService {

    @Resource
    private PositionMapper positionMapper;

    @Autowired
    private QueryConditionFactory conditionFactory;

    @Autowired
    private BeanTransFactory transFactory;

    @Autowired
    private DeptManager deptManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    /**
     * 分页查询职位信息
     * @param positionQuery
     * @return
     */
    @Override
    public ResponsePage<PositionDto> queryPagePositions(PositionQuery positionQuery) {
        LambdaQueryWrapper<PositionInfo> queryWrapper = conditionFactory.allEqWithNull2IsNull(positionQuery, PositionInfo.class)
                .in(PositionInfo::getPositionStatus,DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN)
                .orderByAsc(PositionInfo::getDeptId);
        WhiteLambdaQueryWrapper<PositionInfo> whiteQueryWrapper = conditionFactory.createWhiteQueryWrapper(queryWrapper);
        queryWrapper = whiteQueryWrapper.between(PositionInfo::getCreateTime,positionQuery.getStartTime(),positionQuery.getEndTime(),false)
                .getLambdaQueryWrapper();
        Page<PositionInfo> page = conditionFactory.createPage(positionQuery.getPage(), positionQuery.getSize(), PositionInfo.class);
        Page<PositionInfo> result = this.page(page, queryWrapper);
        List<PositionDto> list = result.getRecords().stream()
                .map(record -> transFactory.trans(record, PositionDto.class))
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
                .in(PositionInfo::getPositionStatus,DogBaseConstant.ACTIVE_NORMAL, DogBaseConstant.DISABLE_WARN)
                .orderByAsc(PositionInfo::getPositionSort);
        List<PositionInfo> list = this.list(queryCondition);
        List<PositionDto> result = list.stream()
                .map(positionInfo -> transFactory.trans(positionInfo, PositionDto.class)).collect(Collectors.toList());
        return result;
    }

    /**
     * 添加职位信息
     * @param position
     */
    @Override
    public void addPosition(PositionInfo position) {
        LambdaQueryWrapper<PositionInfo> queryCondition = conditionFactory.getQueryCondition(PositionInfo.class);
        queryCondition.eq(PositionInfo::getPositionCode, position.getPositionCode())
                .in(PositionInfo::getPositionStatus,DogBaseConstant.ACTIVE_NORMAL, DogBaseConstant.DISABLE_WARN);
        int count = this.count(queryCondition);
        if(count > 0) {
            throw new CustomException(ResponseStatusEnum.POSITION_EXISTS);
        }
        DeptInfo deptInfo = deptManager.queryDeptInfoById(position.getDeptId());
        if(deptInfo == null) {
            throw new CustomException(ResponseStatusEnum.NO_THIS_DEPT);
        }
        position.setDeptCode(deptInfo.getDeptCode());
        position.setDeptName(deptInfo.getDeptName());
        //非必要数据处理
        position.setPositionId(null);
        position.setPositionStatus(DogBaseConstant.ACTIVE_NORMAL);
        position.setCreateUserId(jwtTokenUtil.getUserId());
        position.setCreateTime(new Date());
        this.save(position);
    }

    /**
     * 更新职位信息
     * @param position
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updatePosition(PositionInfo position) {
        LambdaQueryWrapper<PositionInfo> queryCondition = conditionFactory.getQueryCondition(PositionInfo.class);
        queryCondition.eq(PositionInfo::getPositionId,position.getPositionId())
                .or().eq(PositionInfo::getPositionCode,position.getPositionCode())
                .in(PositionInfo::getPositionStatus,DogBaseConstant.ACTIVE_NORMAL, DogBaseConstant.DISABLE_WARN);
        List<PositionInfo> list = this.list(queryCondition);
        if(list.size() != 1) {
            throw new CustomException(ResponseStatusEnum.POSITION_INFO_ERROR);
        }
        PositionInfo oldPosition = list.get(0);
        if(!oldPosition.getPositionId().equals(position.getPositionId())) {
            throw new CustomException(ResponseStatusEnum.NO_THIS_POSITION);
        }
        LambdaUpdateWrapper<PositionInfo> updateCondition = conditionFactory.getUpdateCondition(PositionInfo.class);
        updateCondition.eq(PositionInfo::getPositionId,position.getPositionId())
                .set(PositionInfo::getPositionCode,position.getPositionCode())
                .set(PositionInfo::getPositionName,position.getPositionName())
                .set(PositionInfo::getPositionLevel,position.getPositionLevel())
                .set(PositionInfo::getPositionSort,position.getPositionSort())
                .set(PositionInfo::getDescription,position.getDescription())
                .set(PositionInfo::getUpdateUserId,jwtTokenUtil.getUserId())
                .set(PositionInfo::getUpdateTime,new Date());
        this.update(updateCondition);
        //关联表更新 - 部门表/员工表
        if(!oldPosition.getPositionName().equals(position.getPositionName())) {
            positionMapper.updatePositionInfo(position);
        }
    }

    /**
     * 职位状态更新
     * @param positionId
     * @param status
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updatePositionStatus(Long positionId, int status) {
        LambdaUpdateWrapper<PositionInfo> updateCondition = conditionFactory.getUpdateCondition(PositionInfo.class);
        updateCondition.eq(PositionInfo::getPositionId,positionId)
                .set(PositionInfo::getPositionStatus,status);
        this.update(updateCondition);
        if(status == DogBaseConstant.DELETE_ERROR) {
            positionMapper.updatePositionWithNull(positionId);
        }
    }
}
