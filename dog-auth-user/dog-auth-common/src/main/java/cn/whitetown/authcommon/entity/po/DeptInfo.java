package cn.whitetown.authcommon.entity.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 部门信息
 * @author taixian
 * @date 2020/07/15
 **/
@Getter
@Setter
public class DeptInfo {
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
     * 部门负责人ID
     */
    private Long bossUserId;
    /**
     * 负责人姓名
     */
    private String bossName;
    /**
     * 办公地址
     */
    private String address;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 部门职责/描述
     */
    private String description;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建人ID
     */
    private Long createUserId;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新人ID
     */
    private Long updateUserId;
}