package cn.whitetown.logserver.modo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 接口操作日志信息
 * @author taixian
 * @date 2020/08/13
 **/
@Getter
@Setter
public class OpBaseLog {
    private Long id;
    private String userId;
    private String logLevel;
    private String uriPrefix;
    private String uri;
    private String clientIp;
    private String browser;
    private String resStatus;
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss.SSS")
    @JSONField(format = "yyyy-MM-dd  HH:mm:ss.SSS")
    private Date requestTime;
    /**
     * 响应时长
     */
    private long resTime;

    private String details;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
