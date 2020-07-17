package cn.whitetown.authcommon.entity.ao;

import cn.whitetown.dogbase.common.entity.ao.PageQuery;
import cn.whitetown.dogbase.db.annotation.QueryField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author taixian
 * @date 2020/07/16
 **/
@Getter
@Setter
public class DeptQuery extends PageQuery {
    /**
     * 部门编码
     */
    private String deptCode;
    /**
     * 部门名称
     */
    @QueryField(operation = "like")
    private String deptName;
    /**
     * 负责人姓名
     */
    private String bossName;
    /**
     * 办公地址
     */
    @QueryField(operation = "like")
    private String address;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 部门职责/描述
     */
    @QueryField(operation = "like")
    private String description;

    @QueryField(ignore = true)
    private String detail;
}
