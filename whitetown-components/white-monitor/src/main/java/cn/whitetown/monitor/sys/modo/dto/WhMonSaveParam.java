package cn.whitetown.monitor.sys.modo.dto;

import cn.whitetown.monitor.dao.WhiteTableKey;
import cn.whitetown.monitor.sys.modo.po.*;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 监控数据保存数据库结构
 * @author taixian
 * @date 2020/08/08
 **/
@Getter
@Setter
public class WhMonSaveParam {
    @WhiteTableKey
    private Long id;
    private String serverId;
    private String sysBaseInfo;
    private String whiteCpuInfo;
    private String memInfo;
    private String jvmInfo;
    private String fileInfos;
    private String netInfo;
    private String netSpeed;
    private String timeStamp;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
