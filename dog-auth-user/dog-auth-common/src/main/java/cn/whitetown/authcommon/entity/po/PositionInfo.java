package cn.whitetown.authcommon.entity.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.spring.web.json.Json;

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
    private Long deptId;
    private String deptCode;
    private String deptName;
    private String positionCode;
    private String positionName;
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
