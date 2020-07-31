package cn.whitetown.monitor.sys.modo.po;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件系统信息
 * @author taixian
 * @date 2020/07/31
 **/
@Setter
@Getter
public class WhiteFileInfo {
    private String devName;
    private long fileTotal;
    private long fileFree;
    private long fileRead;
    private long fileWrite;

    private long timeStamp;

    public WhiteFileInfo() {
    }

    public WhiteFileInfo(String devName, long fileTotal, long fileFree, long fileRead, long fileWrite) {
        this.devName = devName;
        this.fileTotal = fileTotal;
        this.fileFree = fileFree;
        this.fileRead = fileRead;
        this.fileWrite = fileWrite;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
