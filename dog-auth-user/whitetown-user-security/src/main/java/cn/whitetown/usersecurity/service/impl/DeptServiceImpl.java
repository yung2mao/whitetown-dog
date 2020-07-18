package cn.whitetown.usersecurity.service.impl;

import cn.whitetown.authcommon.entity.ao.DeptQuery;
import cn.whitetown.authcommon.entity.dto.DeptInfoDto;
import cn.whitetown.dogbase.common.entity.dto.ResponsePage;
import cn.whitetown.dogbase.common.util.DataCheckUtil;
import cn.whitetown.usersecurity.mappers.DeptInfoMapper;
import cn.whitetown.usersecurity.service.DeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 部门管理
 * @author taixian
 * @date 2020/07/16
 **/
@Service
public class DeptServiceImpl implements DeptService {

    @Resource
    private DeptInfoMapper deptMapper;
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
        return null;
    }
}
