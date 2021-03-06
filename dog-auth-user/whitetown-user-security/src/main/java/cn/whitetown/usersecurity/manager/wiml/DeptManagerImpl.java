package cn.whitetown.usersecurity.manager.wiml;

import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.usersecurity.manager.DeptManager;
import cn.whitetown.usersecurity.mappers.DeptInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门通用信息管理
 * @author taixian
 * @date 2020/07/20
 **/
@Service
public class DeptManagerImpl implements DeptManager {
    @Resource
    private DeptInfoMapper deptInfoMapper;

    @Override
    public DeptInfo queryDeptInfoById(Long deptId) {
        if(deptId == null){
            return null;
        }
        return deptInfoMapper.selectById(deptId);
    }

    @Override
    public List<DeptInfo> queryDeptInfoTreeList(Long deptId, Integer lowLevel) {
        if(deptId == null || lowLevel == null){
            return new ArrayList<>();
        }
        return deptInfoMapper.selectDeptTreeList(deptId,lowLevel);
    }

    @Override
    public void updatePositionInfo(Long positionId, Long userId, String realName) {
        deptInfoMapper.updateDeptBossInfo(positionId,userId,realName);
    }
}
