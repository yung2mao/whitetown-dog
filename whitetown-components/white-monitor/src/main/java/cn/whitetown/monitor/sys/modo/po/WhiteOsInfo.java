package cn.whitetown.monitor.sys.modo.po;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统信息
 * @author taixian
 * @date 2020/07/31
 *
 * Description 操作系统信息
 */
@Getter
@Setter
public class WhiteOsInfo implements Serializable {
    private static final long serialVersionUID = 8734157106903476086L;
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

    private long timeStamp;

    public WhiteOsInfo() {
    }

    public WhiteOsInfo(String serverId, String vendor, String description, String version, String arch) {
        this.serverId = serverId;
        this.vendor = vendor;
        this.description = description;
        this.version = version;
        this.arch = arch;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String writeFormat(){
        return new StringBuilder("")
                .append(serverId).append("|")
                .append(vendor).append("|")
                .append(description).append("|")
                .append(version).append("|")
                .append(arch).toString();

    }
}
