package cn.whitetown.authcommon.entity.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 职位信息
 * @author taixian
 * @date 2020/07/20
 **/
@Getter
@Setter
@TableName("position_info")
public class PositionInfo {
    @TableId(type = IdType.AUTO)
    private Long positionId;
    @NotNull(message = "部门ID不能为空")
    private Long deptId;
    private String deptCode;
    private String deptName;
    @NotBlank(message = "职位编码不能为空")
    private String positionCode;
    @NotBlank(message = "职位名称不能为空")
    private String positionName;
    @NotNull(message = "职位类别不能为空")
    @Min(value = 1,message = "只能为1或2")
    @Max(value = 2,message = "只能为1或2")
    private Integer positionLevel;
    private Integer positionSort;
    private String description;
    private Integer positionStatus;
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date createTime;
    private Long createUserId;
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date updateTime;
    private Long updateUserId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
