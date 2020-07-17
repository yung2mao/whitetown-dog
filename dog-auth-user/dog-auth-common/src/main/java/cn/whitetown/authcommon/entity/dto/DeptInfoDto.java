package cn.whitetown.authcommon.entity.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 部门信息展示
 * @author taixian
 * @date 2020/07/16
 **/
@Getter
@Setter
public class DeptInfoDto {
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
