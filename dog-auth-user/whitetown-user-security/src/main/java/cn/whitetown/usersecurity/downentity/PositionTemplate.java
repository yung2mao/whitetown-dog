package cn.whitetown.usersecurity.downentity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 职位导出excel template
 * @author taixian
 * @date 2020/07/20
 **/
@Getter
@Setter
public class PositionTemplate {
    @ExcelIgnore
    private Long positionId;

    @ExcelIgnore
    private Long deptId;

    @ExcelProperty("部门编码")
    @ColumnWidth(20)
    private String deptCode;

    @ExcelProperty("部门名称")
    @ColumnWidth(15)
    private String deptName;

    @ExcelProperty("职位编码")
    @ColumnWidth(30)
    private String positionCode;

    @ExcelProperty("职位名称")
    @ColumnWidth(15)
    private String positionName;

    @ExcelProperty("职位级别")
    @ColumnWidth(15)
    private Integer positionLevel;

    @ExcelProperty("职位排序")
    @ColumnWidth(15)
    private Integer positionSort;

    @ExcelProperty("职位描述")
    @ColumnWidth(25)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss")
    @ExcelProperty("创建时间")
    @ColumnWidth(25)
    private Date createTime;
}
