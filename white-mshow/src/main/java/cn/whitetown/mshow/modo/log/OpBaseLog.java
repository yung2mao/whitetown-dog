package cn.whitetown.mshow.modo.log;

import cn.whitetown.esconfig.annotation.EsFieldConfig;
import cn.whitetown.esconfig.config.EsConfigEnum;
import com.alibaba.fastjson.JSON;
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
    @EsFieldConfig(name = "browser", config = {EsConfigEnum.TEXT,EsConfigEnum.ES_STANDARD})
    private String browser;
    private String resStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date requestTime;
    /**
     * 响应时长
     */
    private long resTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
