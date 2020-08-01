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
    /**
     * 名称
     */
    private String name;
    /**
     * 盘符
     */
    private String mount;
    /**
     * 描述
     */
    private String description;
    /**
     * 文件类型
     */
    private String fsType;
    private long fileTotal;
    private long fileUsed;
    private long fileFree;

    private long timeStamp;

    public WhiteFileInfo() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
