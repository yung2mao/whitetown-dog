package cn.whitetown.authcommon.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 职位信息展示
 * @author taixian
 * @date 2020/07/20
 **/
@Getter
@Setter
public class PositionDto {
    private Long positionId;
    private Long deptId;
    private String deptCode;
    private String deptName;
    private String positionCode;
    private String positionName;
    private Integer positionLevel;
    private Integer positionSort;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    private Date createTime;
}
