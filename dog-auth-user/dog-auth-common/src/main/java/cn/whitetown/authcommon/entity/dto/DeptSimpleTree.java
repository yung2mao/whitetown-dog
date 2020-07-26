package cn.whitetown.authcommon.entity.dto;


import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本信息的部门树形结构
 * @author taixian
 * @date 2020/07/25
 **/
@Getter
@Setter
public class DeptSimpleTree {
    private Long deptId;
    private String deptCode;
    private String deptName;
    private Integer deptStatus;
    private List<DeptSimpleTree> children = new ArrayList<>();

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
