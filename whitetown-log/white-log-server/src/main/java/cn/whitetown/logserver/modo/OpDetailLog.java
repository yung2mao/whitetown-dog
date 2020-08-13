package cn.whitetown.logserver.modo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author taixian
 * @date 2020/08/13
 **/
public class OpDetailLog {
    private Long id;
    private String logName;
    private String logLevel;
    private Integer status;
    private String details;
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss.SSS")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss.SSS")
    private Date logTime;

}
