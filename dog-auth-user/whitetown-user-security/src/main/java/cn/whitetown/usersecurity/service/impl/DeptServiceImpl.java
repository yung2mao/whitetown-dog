package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.authcommon.entity.dto.DeptSimpleDto;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.authcommon.entity.po.PositionInfo;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.authcommon.util.token.JwtTokenUtil;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.CustomException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.entity.WhiteLambdaQueryWrapper;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.PositionManager;
import cn.whitetown.usersecurity.manager.UserManager;
import cn.whitetown.usersecurity.mappers.DeptInfoMapper;
import cn.whitetown.usersecurity.service.DeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理
 * @author taixian
 * @date 2020/07/16
 **/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptInfoMapper,DeptInfo> implements DeptService {

    @Resource
    private DeptInfoMapper deptMapper;

    @Autowired
    private QueryConditionFactory conditionFactory;

    @Autowired
    private PositionManager positionManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BeanTransFactory transFactory;
    /**
     * 部门信息检索
     * detail - 允许为编码或名称
     * @param deptQuery
     * @return
     */
    @Override
    public ResponsePage<DeptInfoDto> searchDeptInfos(DeptQuery deptQuery) {
        if(!DataCheckUtil.checkTextNullBool(deptQuery.getDetail())){
            String detail = deptQuery.getDetail();
            String codeRegex = "[a-zA-Z_]+";
            if(detail.matches(codeRegex)){
                deptQuery.setDeptCode(detail);
            }else {
                deptQuery.setDeptName(detail);
            }
        }
        LambdaQueryWrapper<DeptInfo> queryCondition = conditionFactory.allEqWithNull2IsNull(deptQuery,DeptInfo.class);
        queryCondition.in(DeptInfo::getDeptStatus,DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN)
                .gt(DeptInfo::getDeptId,AuthConstant.ROOT_DEPT_ID);
        WhiteLambdaQueryWrapper<DeptInfo> whiteQueryWrapper = conditionFactory.createWhiteQueryWrapper(queryCondition);
        queryCondition = whiteQueryWrapper.between(DeptInfo::getCreateTime,deptQuery.getStartTime(), deptQuery.getEndTime(),false)
                .getLambdaQueryWrapper();
        Page<DeptInfo> page = conditionFactory.createPage(deptQuery.getPage(), deptQuery.getSize(), DeptInfo.class);
        Page<DeptInfo> pageResult = this.page(page, queryCondition);
        List<DeptInfo> records = pageResult.getRecords();
        List<DeptInfoDto> result = records.stream().map(deptInfo -> transFactory.trans(deptInfo, DeptInfoDto.class))
                .collect(Collectors.toList());
        return ResponsePage.createPage(pageResult.getCurrent(),pageResult.getSize(),pageResult.getTotal(),result);
    }

    /**
     * 返回所有部门简化信息集合
     * @return
     */
    @Override
    public List<DeptSimpleDto> searchAllSimpleDept() {
        LambdaQueryWrapper<DeptInfo> queryCondition = conditionFactory.getQueryCondition(DeptInfo.class);
        queryCondition.in(DeptInfo::getDeptStatus,DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN)
                .lt(DeptInfo::getDeptId,AuthConstant.ROOT_DEPT_ID);
        queryCondition.select(DeptInfo::getDeptId,
                DeptInfo::getDeptCode,
                DeptInfo::getDeptName,
                DeptInfo::getDeptStatus);
        List<DeptInfo> list = this.list(queryCondition);
        return list.stream().map(deptInfo -> transFactory.trans(deptInfo,DeptSimpleDto.class))
                .collect(Collectors.toList());
    }

    /**
     * 添加部门信息
     * @param deptInfo
     */
    @Override
    public void addDeptInfo(DeptInfo deptInfo) {
        LambdaQueryWrapper<DeptInfo> queryCondition = conditionFactory.getQueryCondition(DeptInfo.class);
        queryCondition.eq(DeptInfo::getDeptCode,deptInfo.getDeptCode());
        if(deptInfo.getParentId() != null) {
            queryCondition.or().eq(DeptInfo::getParentId,deptInfo.getDeptId());
        }
        List<DeptInfo> list = this.list(queryCondition);
        //如果有parentId,则应该查到一个部门信息,否则没有
        boolean isRealSize = deptInfo.getParentId() == null ? list.size() == 0 : list.size() == 1;
        if(!isRealSize){
            throw new CustomException(ResponseStatusEnum.DEPT_INFO_ERROR);
        }
        if(list.size() == 1){
            if(!list.get(0).equals(deptInfo.getParentId())) {
                throw new CustomException(ResponseStatusEnum.DEPT_EXISTS);
            }
        }
        //数据合法性处理,同时去除此处不添加的信息
        if(deptInfo.getParentId() == null){
            deptInfo.setParentId(AuthConstant.ROOT_DEPT_ID);
            deptInfo.setDeptLevel(AuthConstant.DEFAULT_DEPT_LEVEL);
        }else {
            deptInfo.setDeptLevel(list.get(0).getDeptLevel()+1);
        }
        deptInfo.setDeptId(null);
        deptInfo.setDeptStatus(DogBaseConstant.ACTIVE_NORMAL);
        deptInfo.setBossPositionId(null);
        deptInfo.setBossPositionName(null);
        deptInfo.setBossUserId(null);
        deptInfo.setBossName(null);
        deptInfo.setCreateUserId(jwtTokenUtil.getUserId());
        deptInfo.setCreateTime(new Date());
        this.save(deptInfo);
    }

    /**
     * 部门信息更新
     * @param deptInfo
     */
    @Override
    public void updateDeptInfo(DeptInfo deptInfo) {
        LambdaQueryWrapper<DeptInfo> queryCondition = conditionFactory.getQueryCondition(DeptInfo.class);
        queryCondition.in(DeptInfo::getDeptId,deptInfo.getDeptId(),deptInfo.getParentId())
                .or().eq(DeptInfo::getDeptCode,deptInfo.getDeptCode());
        List<DeptInfo> list = this.list(queryCondition);
        DeptInfo oldDept = null;
        DeptInfo parentDept = null;
        int currentDeptAndParentSize = 2;
        if(list.size() == currentDeptAndParentSize){
            for (DeptInfo dept : list) {
                if(dept.getDeptId().equals(deptInfo.getDeptId())){
                    oldDept = dept;
                }else if (dept.getDeptId().equals(deptInfo.getParentId())){
                    parentDept = dept;
                }
            }
            if(oldDept == null || parentDept == null){
                throw new CustomException(ResponseStatusEnum.ERROR_PARAMS);
            }
        }else {
            throw new CustomException(ResponseStatusEnum.ERROR_PARAMS);
        }
        deptInfo.setDeptLevel(parentDept.getDeptLevel()+1);
        deptInfo.setDeptStatus(oldDept.getDeptStatus());
        deptInfo.setBossPositionId(oldDept.getBossPositionId());
        deptInfo.setBossPositionName(oldDept.getBossPositionName());
        deptInfo.setBossUserId(oldDept.getBossUserId());
        deptInfo.setBossName(oldDept.getBossName());
        deptInfo.setCreateUserId(oldDept.getCreateUserId());
        deptInfo.setCreateTime(oldDept.getCreateTime());
        deptInfo.setUpdateUserId(jwtTokenUtil.getUserId());
        deptInfo.setUpdateTime(new Date());
        this.updateById(deptInfo);
    }

    /**
     * 部门负责人信息分配
     * @param deptId
     * @param positionId
     * @param userId
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void configureBoss(Long deptId, Long positionId, Long userId) {
        PositionInfo position = positionManager.queryPositionByIdAndDeptId(deptId, positionId);
        if(position == null || position.getPositionLevel() != AuthConstant.ONE_PERSON_LEVEL) {
            throw new CustomException(ResponseStatusEnum.NO_THIS_POSITION);
        }
        LambdaUpdateWrapper<DeptInfo> updateWrapper = conditionFactory.getUpdateCondition(DeptInfo.class)
                .eq(DeptInfo::getDeptId,deptId)
                .set(DeptInfo::getBossPositionId,position.getPositionId())
                .set(DeptInfo::getBossPositionName,position.getPositionName());
        if(userId != null) {
            LambdaQueryWrapper<UserBasicInfo> queryWrapper = conditionFactory.getQueryCondition(UserBasicInfo.class)
                    .eq(UserBasicInfo::getUserId,userId);
            List<UserBasicInfo> users = userManager.getUserByWrapper(queryWrapper);
            if(users.size() != 1) {
                throw new CustomException(ResponseStatusEnum.NO_THIS_USER);
            }
            updateWrapper.set(DeptInfo::getBossUserId,userId);
            updateWrapper.set(DeptInfo::getBossName,users.get(0).getRealName());
            deptMapper.updateUserDeptInfoIfExists(deptId,positionId,userId);
        }
        this.update(updateWrapper);
    }

    /**
     * 部门状态变更
     * @param deptId
     * @param deptStatus
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateDeptStatus(Long deptId, Integer deptStatus) {
        DeptInfo dept = this.getById(deptId);
        if(dept==null || dept.getDeptStatus() == DogBaseConstant.DELETE_ERROR){
            throw new CustomException(ResponseStatusEnum.NO_THIS_DEPT);
        }
        if(deptStatus == DogBaseConstant.DELETE_ERROR) {
            deptMapper.delDeptStatusAndRelationSystem(deptId, deptStatus);
            return;
        }
        LambdaUpdateWrapper<DeptInfo> updateCondition = conditionFactory.getUpdateCondition(DeptInfo.class);
        updateCondition.eq(DeptInfo::getDeptId,deptId)
                .set(DeptInfo::getDeptStatus,deptStatus);
        this.update(updateCondition);
    }

}
