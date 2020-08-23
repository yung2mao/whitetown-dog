package cn.whitetown.usersecurity.downentity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

/**
 * @author taixian
 * @date 2020/08/23
 **/
@Setter
@Getter
public class RoleExcelTemplate {
    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Long roleId;
    /**
     * 角色名称
     */
    @ExcelProperty("角色名称")
    @ColumnWidth(15)
    private String name;
    /**
     * 角色描述
     */
    @ExcelProperty("角色描述")
    @ColumnWidth(25)
    private String description;
    /**
     * 排序 - 如果为空顺次排列
     */
    @ExcelProperty("角色排序")
    @ColumnWidth(15)
    private Integer sort;
    /**
     * 角色状态
     */
    @ExcelProperty("角色状态")
    @ColumnWidth(15)
    private Integer roleStatus;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    @ColumnWidth(25)
    private Date createTime;
    /**
     * 最后更新
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("更新时间")
    @ColumnWidth(25)
    private Date updateTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
