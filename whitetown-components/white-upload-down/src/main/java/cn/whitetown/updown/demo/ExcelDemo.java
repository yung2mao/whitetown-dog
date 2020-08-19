package cn.whitetown.updown.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author taixian
 * @date 2020/08/19
 **/
@Getter
@Setter
public class ExcelDemo {
    @ExcelProperty(value = "ID",index = 0)
    private String id;

    @ExcelProperty(value = "用户名", index = 1)
    private String username;

    @ExcelProperty(value = "电话",index = 2)
    private String telephone;

    @ExcelProperty(value = "创建时间",index = 3)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
