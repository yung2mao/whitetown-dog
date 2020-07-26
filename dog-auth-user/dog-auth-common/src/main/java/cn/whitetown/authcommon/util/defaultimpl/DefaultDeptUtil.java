package cn.whitetown.authcommon.util.defaultimpl;

import cn.whitetown.authcommon.entity.dto.DeptInfoTree;
import cn.whitetown.authcommon.entity.dto.DeptSimpleTree;
import cn.whitetown.authcommon.entity.po.DeptInfo;
import cn.whitetown.authcommon.util.DeptUtil;
import cn.whitetown.dogbase.db.factory.BeanTransFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

/**
 * @author taixian
 * @date 2020/07/25
 **/
public class DefaultDeptUtil implements DeptUtil {

    @Autowired
    private BeanTransFactory transFactory;

    @Override
    public DeptSimpleTree createDeptSimpleTree(List<DeptInfo> deptInfos) {
        return this.createDeptSimpleTree(null,deptInfos);
    }

    @Override
    public DeptInfoTree createDeptDetailTree(List<DeptInfo> deptInfos) {
        return this.createDeptDetailTree(null,deptInfos);
    }

    /**
     * 根据父级部门和部门List创建简化部门树
     * @param deptSimpleTree
     * @param deptInfos
     * @return
     */
    public DeptSimpleTree createDeptSimpleTree(DeptSimpleTree deptSimpleTree,List<DeptInfo> deptInfos) {
        if(deptSimpleTree == null) {
            DeptInfo parentDept = deptInfos.stream().min(Comparator.comparing(DeptInfo::getDeptLevel)).get();
            deptInfos.remove(parentDept);
            deptSimpleTree = transFactory.trans(parentDept,DeptSimpleTree.class);
        }
        for(DeptInfo deptInfo : deptInfos) {
            if(deptInfo.getParentId().equals(deptSimpleTree.getDeptId())) {
                DeptSimpleTree childTree = transFactory.trans(deptInfo, DeptSimpleTree.class);
                deptSimpleTree.getChildren().add(childTree);
                this.createDeptSimpleTree(childTree,deptInfos);
            }
        }
        return deptSimpleTree;
    }

    /**
     * 根据父级部门和部门List创建部门详情树
     * @param deptInfoTree
     * @param deptInfos
     * @return
     */
    public DeptInfoTree createDeptDetailTree(DeptInfoTree deptInfoTree,List<DeptInfo> deptInfos) {
        if(deptInfoTree == null) {
            DeptInfo parentDept = deptInfos.stream().min(Comparator.comparing(DeptInfo::getDeptLevel)).get();
            deptInfos.remove(parentDept);
            deptInfoTree = transFactory.trans(parentDept,DeptInfoTree.class);
        }
        for(DeptInfo deptInfo : deptInfos) {
            if(deptInfo.getParentId().equals(deptInfoTree.getDeptId())) {
                DeptInfoTree childTree = transFactory.trans(deptInfo, DeptInfoTree.class);
                deptInfoTree.getChildren().add(childTree);
                this.createDeptDetailTree(childTree,deptInfos);
            }
        }
        return deptInfoTree;
    }
}
