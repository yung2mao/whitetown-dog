package cn.whitetown.monitor.sys.modo.po;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * cpu信息
 * @author taixian
 * @date 2020/07/31
 **/
@Getter
@Setter
public class WhiteCpuInfo {
    /**
     * 名称
     */
    private String name;
    /**
     * 厂商
     */
    private String vendor;
    /**
     * 架构
     */
    private String model;
    /**
     * 总核心数
     */
    private int totalCores;

    /*以下均表示百分比的int数值,例如97.51%则为9751*/

    private int userUse;
    private int sysUse;
    private int idle;
    private int wait;
    private int free;

    private long timeStamp;

    public WhiteCpuInfo() {
    }

    public WhiteCpuInfo(String vendor, String model, int totalCores, int userUse, int sysUse, int idle, int wait, int free) {
        this.vendor = vendor;
        this.model = model;
        this.totalCores = totalCores;
        this.userUse = userUse;
        this.sysUse = sysUse;
        this.idle = idle;
        this.wait = wait;
        this.free = free;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
