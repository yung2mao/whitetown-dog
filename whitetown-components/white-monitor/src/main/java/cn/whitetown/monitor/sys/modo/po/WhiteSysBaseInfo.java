package cn.whitetown.monitor.sys.modo.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 系统信息
 * @author taixian
 * @date 2020/07/31
 *
 * Description 操作系统信息
 */
@Getter
@Setter
public class WhiteSysBaseInfo {
    /**
     * 服务器id
     */
    private String serverId;
    /**
     * 系统厂商
     */
    private String vendor;
    /**
     * 系统描述
     */
    private String description;
    /**
     * 系统版本
     */
    private String version;
    /**
     * 系统架构
     */
    private String arch;
    /**
     * cpu温度
     */
    private String cpuTemperature;
    /**
     * 风扇转速
     */
    private long fanSpeed;
    /**
     * 服务启动时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 工作时长
     */
    private String workTime;

    private long timeStamp;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
