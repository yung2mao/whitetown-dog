package cn.whitetown.usersecurity.manager.impl;

import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.usersecurity.manager.DeptManager;
import cn.whitetown.usersecurity.mappers.DeptInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
