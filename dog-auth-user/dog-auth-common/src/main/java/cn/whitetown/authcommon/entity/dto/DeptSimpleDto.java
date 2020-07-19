package cn.whitetown.authcommon.entity.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * @author taixian
 * @date 2020/07/19
 **/
@Getter
@Setter
public class DeptSimpleDto {
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 部门编码
     */
    private String deptCode;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门状态
     */
    private Integer deptStatus;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
