package cn.whitetown.usersecurity.downentity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户基础信息表示层
 * @author GrainRain
 * @date 2020/06/20 16:04
 **/
@Setter
@Getter
public class UserBasicDown {
    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    @ColumnWidth(15)
    private String username;
    /**
     * 真实姓名
     */
    @ExcelProperty("姓名")
    @ColumnWidth(15)
    private String realName;
    /**
     * 生日
     */
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("出生日期")
    @ColumnWidth(25)
    private Date birthday;
    /**
     * 性别
     */
    @ExcelProperty("性别")
    @ColumnWidth(15)
    private String gender;
    /**
     * 所属部门ID
     */
    @ExcelIgnore
    private Long deptId;
    /**
     * 所属部门名称
     */
    @ExcelProperty("部门名称")
    @ColumnWidth(15)
    private String deptName;
    /**
     * 职位ID
     */
    @ExcelIgnore
    private Long positionId;
    /**
     * 职位名称
     */
    @ExcelProperty("职位名称")
    @ColumnWidth(15)
    private String positionName;
    /**
     * 邮箱
     */
    @ExcelProperty("电子邮箱")
    @ColumnWidth(25)
    private String email;
    /**
     * 电话号码
     */
    @ExcelProperty("电话号码")
    @ColumnWidth(15)
    private String telephone;
    /**
     * 用户状态
     * 0 - 正常
     * 1 - 停用
     * 2 - 删除
     */
    @ExcelProperty("用户状态")
    @ColumnWidth(15)
    private Integer userStatus;
    /**
     * 创建时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    @ColumnWidth(25)
    private Date createTime;

}
