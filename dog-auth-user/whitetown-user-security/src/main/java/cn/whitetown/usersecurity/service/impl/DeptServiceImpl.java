package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.constant.AuthConstant;
import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.authcommon.entity.dto.DeptInfoTree;
import cn.whitetown.authcommon.entity.dto.DeptSimpleTree;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.authcommon.entity.po.PositionInfo;
import cn.whitetown.authcommon.entity.po.UserBasicInfo;
import cn.whitetown.authcommon.util.DeptUtil;
import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.dogbase.common.constant.DogBaseConstant;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.dogbase.db.entity.WhiteLambdaQueryWrapper;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import cn.whitetown.dogbase.db.factory.QueryConditionFactory;
import cn.whitetown.usersecurity.manager.DeptManager;
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
    private DeptManager deptManager;

    @Autowired
    private DeptUtil deptUtil;

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
    public DeptInfoTree queryDeptDetailTree(Long deptId,Integer lowLevel) {
        List<DeptInfo> deptInfos = deptManager.queryDeptInfoTreeList(deptId, lowLevel);
        if(deptInfos.size() == 0) {
            return new DeptInfoTree();
        }
        DeptInfoTree deptInfoTree = deptUtil.createDeptDetailTree(deptInfos);
        return deptInfoTree;
    }

    /**
     * 查询简化部门树
     * @param deptId
     * @param lowLevel
     * @return
     */
    @Override
    public DeptSimpleTree querySimpleTree(Long deptId, Integer lowLevel) {
        List<DeptInfo> deptInfos = deptManager.queryDeptInfoTreeList(deptId,lowLevel);
        return deptInfos.size() == 0 ? new DeptSimpleTree() : deptUtil.createDeptSimpleTree(deptInfos);
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
            queryCondition.or().eq(DeptInfo::getDeptId,deptInfo.getParentId());
        }
        queryCondition.in(DeptInfo::getDeptStatus,DogBaseConstant.ACTIVE_NORMAL,DogBaseConstant.DISABLE_WARN);
        List<DeptInfo> list = this.list(queryCondition);
        //如果有parentId,则应该查到一个部门信息,否则没有
        boolean isRealSize = deptInfo.getParentId() == null ? list.size() == 0 : list.size() == 1;
        if(!isRealSize){
            throw new WhResException(ResponseStatusEnum.DEPT_INFO_ERROR);
        }
        if(list.size() == 1){
            if(!list.get(0).getDeptId().equals(deptInfo.getParentId())) {
                throw new WhResException(ResponseStatusEnum.DEPT_EXISTS);
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
        // check exists and parent dept
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
                throw new WhResException(ResponseStatusEnum.DEPT_INFO_ERROR);
            }
        }else {
            throw new WhResException(ResponseStatusEnum.DEPT_INFO_ERROR);
        }
        //check position / if change
        Long checkPositionUserId = null;
        if(deptInfo.getBossPositionId() != null && !deptInfo.getBossPositionId().equals(oldDept.getBossPositionId())) {
            PositionInfo positionInfo = positionManager.queryPositionById(deptInfo.getBossPositionId());
            if(positionInfo == null) {
                throw new WhResException(ResponseStatusEnum.NO_THIS_POSITION);
            }
            deptInfo.setBossPositionName(positionInfo.getPositionName());
            if(positionInfo.getPositionLevel() == AuthConstant.ONE_PERSON_LEVEL) {
                checkPositionUserId = positionInfo.getPositionId();
            }
        }else {
            deptInfo.setBossPositionId(oldDept.getBossPositionId());
            deptInfo.setBossPositionName(oldDept.getBossPositionName());
        }
        //set new boss / if position is changed
        boolean setNewBoss = false;
        if(checkPositionUserId != null) {
            LambdaQueryWrapper<UserBasicInfo> userQuery = conditionFactory.getQueryCondition(UserBasicInfo.class)
                    .eq(UserBasicInfo::getPositionId,checkPositionUserId)
                    .select(UserBasicInfo::getUserId,UserBasicInfo::getRealName);
            List<UserBasicInfo> userList = userManager.getUserByWrapper(userQuery);
            if(userList.size() == 1) {
                deptInfo.setBossUserId(userList.get(0).getUserId());
                deptInfo.setBossName(userList.get(0).getRealName());
                setNewBoss = true;
            }
        }
        if(!setNewBoss) {
            deptInfo.setBossUserId(oldDept.getBossUserId());
            deptInfo.setBossName(oldDept.getBossName());
        }
        deptInfo.setDeptLevel(parentDept.getDeptLevel()+1);
        deptInfo.setDeptStatus(oldDept.getDeptStatus());
        deptInfo.setCreateUserId(oldDept.getCreateUserId());
        deptInfo.setCreateTime(oldDept.getCreateTime());
        deptInfo.setUpdateUserId(jwtTokenUtil.getUserId());
        deptInfo.setUpdateTime(new Date());
        this.updateById(deptInfo);
    }

    /**
     * 指定部门负责人
     * @param deptId
     * @param username
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void configureBoss(Long deptId, String username) {
        LambdaQueryWrapper<DeptInfo> queryWrapper = conditionFactory.getQueryCondition(DeptInfo.class)
                .eq(DeptInfo::getDeptId, deptId)
                .in(DeptInfo::getDeptStatus, DogBaseConstant.ACTIVE_NORMAL, DogBaseConstant.DISABLE_WARN)
                .select(DeptInfo::getDeptId);
        DeptInfo deptInfo = this.getOne(queryWrapper);
        if(deptInfo == null) {
            throw new WhResException(ResponseStatusEnum.NO_THIS_DEPT);
        }
        UserBasicInfo userInfo = userManager.getUserByUsername(username);
        if(userInfo == null) {
            throw new WhResException(ResponseStatusEnum.NO_THIS_USER);
        }
        LambdaUpdateWrapper<DeptInfo> updateWrapper = conditionFactory.getUpdateCondition(DeptInfo.class)
                .eq(DeptInfo::getDeptId, deptId)
                .set(DeptInfo::getBossUserId, userInfo.getUserId())
                .set(DeptInfo::getBossName, userInfo.getRealName())
                .set(DeptInfo::getUpdateUserId, jwtTokenUtil.getUserId())
                .set(DeptInfo::getUpdateTime, new Date());
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
            throw new WhResException(ResponseStatusEnum.NO_THIS_DEPT);
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
