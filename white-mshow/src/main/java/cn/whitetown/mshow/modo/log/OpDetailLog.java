package cn.whitetown.mshow.modo.log;

import cn.whitetown.esconfig.annotation.EsFieldConfig;
import cn.whitetown.esconfig.config.EsConfigEnum;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 接口操作日志 - 细节信息
 * @author taixian
 * @date 2020/08/13
 **/
@Setter
@Getter
public class OpDetailLog {
    private Long id;
    private String logLevel;
    private Integer status;

    @EsFieldConfig(name = "message",config = {EsConfigEnum.TEXT,EsConfigEnum.ES_IK})
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date logTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
