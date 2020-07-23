package cn.whitetown.authcommon.entity.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Target;
import java.util.Date;

/**
 * 部门信息
 * @author taixian
 * @date 2020/07/15
 **/
@Getter
@Setter
@TableName("dept_info")
public class DeptInfo {
    /**
     * 部门ID
     */
    @TableId(type = IdType.AUTO)
    @Min(value = 2,message = "顶级部门信息禁止变更")
    private Long deptId;
    /**
     * 部门编码
     */
    @NotBlank(message = "部门编码禁止为空")
    private String deptCode;
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称禁止为空")
    private String deptName;
    /**
     * 父级部门ID
     */
    private Long parentId;
    /**
     * 部门层级
     */
    private Integer deptLevel;
    /**
     * 理论负责人职位ID
     */
    private Long bossPositionId;
    /**
     * 理论负责人职位名称
     */
    private String bossPositionName;
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
    @Pattern(regexp = "\\d+")
    private String phone;
    /**
     * 部门职责/描述
     */
    private String description;
    /**
     * 部门状态
     */
    private Integer deptStatus;
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
