package cn.whitetown.monitor.sys.modo.po;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * Jvm信息
 * @author taixian
 * @date 2020/07/31
 **/
@Getter
@Setter
public class WhiteJvmInfo {
    private String jvmVersion;
    private String jdkPath;
    private long jvmMax;
    private long jvmTotal;
    private long jvmFree;
    private int jvmUsedPercent;

    private long timeStamp;

    public WhiteJvmInfo() {
    }

    public WhiteJvmInfo(String jvmVersion, String jdkPath, long jvmMax, long jvmTotal, long jvmFree, int jvmUsedPercent) {
        this.jvmVersion = jvmVersion;
        this.jdkPath = jdkPath;
        this.jvmMax = jvmMax;
        this.jvmTotal = jvmTotal;
        this.jvmFree = jvmFree;
        this.jvmUsedPercent = jvmUsedPercent;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
